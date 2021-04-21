package com.graduate.user.controller;


import com.alibaba.fastjson.JSON;
import com.graduate.handler.AliyunOSSUtil;
import com.graduate.user.entity.User;
import com.graduate.user.mapper.UserMapper;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import javafx.geometry.Pos;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.yan.Tools.GenerateImage;
import static com.yan.Tools.base64ToImageOutput;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author YanJiewei
 * @since 2021-03-30
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/login")
    public String login(@RequestBody User user){
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> res = new HashMap<>();
        //自定义查询条件
        //把查询条件放进map，mybatis-plus会将条件自动拼接
        map.put("telnum", user.getTelnum());
        List<User> list = userMapper.selectByMap(map);
        if(list.size() == 0){
            //还没注册
            res.put("flag", "400");
        }
        else{
            User u = list.get(0);
            System.out.println(DigestUtils.md5Hex(user.getPassword() + u.getSalt()));
            System.out.println(user.getPassword());
            if(DigestUtils.md5Hex(user.getPassword() + u.getSalt()).equals(u.getPassword())){
                //已注册并且密码正确
                res.put("flag", "200");
                res.put("user", u);
            }
            else{
                //已注册但是密码错误
                res.put("flag", "500");
            }
        }
        return JSON.toJSONString(res);
    }

    @PostMapping("/register")
    public String register(@RequestBody User user){
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> res = new HashMap<>();
        //自定义查询条件
        //把查询条件放进map，mybatis-plus会将条件自动拼接
        map.put("telnum", user.getTelnum());
        List<User> list = userMapper.selectByMap(map);
        if(list.size() == 0){
            String salt = UUID.randomUUID().toString();
            user.setSalt(salt);
            System.out.println(user.getPassword());
            System.out.println(DigestUtils.md5Hex(user.getPassword() + salt));
            user.setPassword(DigestUtils.md5Hex(user.getPassword() + salt));
            userMapper.insert(user);
            return "200";
        }
        else{
            //已经注册过了
            return "400";
        }
    }


    @PostMapping("/insert")
    public int insert(@RequestBody User user){
        return userMapper.insert(user);
    }

    @PostMapping("/update")
    public int update(@RequestBody User user){
        int i = userMapper.updateById(user);
        System.out.println(i);
        return i;
    }

    @GetMapping("/findbyid/{id}")
    public User findbyid(@PathVariable("id") Integer id){
        System.out.println(id);
        return userMapper.selectById(id);
    }

    @PostMapping("/uploadavatar")
    public String uploadavatar(@RequestBody Map<String, String> param) throws IOException {
        //base64先保存到本地
        String image = param.get("image");
        String base64Image = image.split(",", 2)[1];
        base64Image = base64Image.replaceAll("%2F", "/");
        String path = System.getProperty("user.dir");
        System.out.println(path);
        String name = UUID.randomUUID().toString() + ".jpg";
        OutputStream out = new FileOutputStream(path + "\\" + name);
        base64ToImageOutput(base64Image, out);
        //然后上传本地文件到阿里云oss
        File file = new File(path + "\\" + name);
        AliyunOSSUtil aliyunOSSUtil = new AliyunOSSUtil();
        String url = aliyunOSSUtil.upLoad(file);
        System.out.println(url);
        //上传完成后删除本地文件
        file.delete();
        String id = param.get("id");
        User user1 = userMapper.selectById(id);
        user1.setAvatar(url);
        userMapper.updateById(user1);
        return JSON.toJSONString(user1);
    }

    @PostMapping("/changepwd")
    public String changepwd(@RequestBody Map<String, String> param) throws IOException {
        String prepwd = param.get("prepwd");
        String pwd = param.get("pwd");
        String id = param.get("id");
        User user = userMapper.selectById(id);
        if(user.getPassword().equals(DigestUtils.md5Hex(prepwd + user.getSalt()))){
            user.setPassword(DigestUtils.md5Hex(pwd + user.getSalt()));
            userMapper.updateById(user);
            return "200";
        }
        else{
            return "400";
        }
    }

    @GetMapping("/getclassmatelist/{id}")
    public List<User> getClassmateList(@PathVariable("id") Integer id){
        User user = userMapper.selectById(id);
        HashMap<String, Object> map = new HashMap<>();
        map.put("class1", user.getClass1());
        map.put("subject", user.getSubject());
        List<User> list = userMapper.selectByMap(map);
        return list;
    }

    //上传个人照片
    @PostMapping("/uploadpersonalbum")
    public String uploadpersonalbum(@RequestBody Map<String, String> param) throws IOException {
        //base64先保存到本地
        String image = param.get("image");
        String base64Image = image.split(",", 2)[1];
        base64Image = base64Image.replaceAll("%2F", "/");
        String path = System.getProperty("user.dir");
        System.out.println(path);
        String name = UUID.randomUUID().toString() + ".jpg";
        OutputStream out = new FileOutputStream(path + "\\" + name);
        base64ToImageOutput(base64Image, out);
        //然后上传本地文件到阿里云oss
        File file = new File(path + "\\" + name);
        AliyunOSSUtil aliyunOSSUtil = new AliyunOSSUtil();
        String url = aliyunOSSUtil.upLoad(file);
        System.out.println(url);
        //上传完成后删除本地文件
        file.delete();
        User user = JSON.parseObject(param.get("user"), User.class);
        User user1 = userMapper.selectById(user.getId());
        user1.setAvatar(url);
        userMapper.updateById(user);
        return JSON.toJSONString(user1);
    }


}

