package com.graduate.user.controller;


import com.alibaba.fastjson.JSON;
import com.graduate.handler.AliyunOSSUtil;
import com.graduate.user.entity.User;
import com.graduate.user.mapper.UserMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.graduate.Utils.RSAUtils.myEncrypt;
import static com.graduate.user.Tools.base64ToImageOutput;
import static com.graduate.user.Tools.judgeAuthority;


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
    public String login(@RequestBody User user) throws Exception {
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
            if(DigestUtils.md5Hex(user.getPassword() + u.getSalt()).equals(u.getPassword())){
                //已注册并且密码正确
                //生成token
                String token = DigestUtils.md5Hex(user.getName() + u.getSalt());
                u.setToken(token);
                userMapper.updateById(u);
                System.out.println(token);
                //返回前端
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
            user.setAvatar("https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png");
            System.out.println(user.getPassword());
            System.out.println(DigestUtils.md5Hex(user.getPassword() + salt));
            user.setPassword(DigestUtils.md5Hex(user.getPassword() + salt));
            user.setHaveauthority(0);
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

    //修改信息
    @PostMapping("/update/{id}/{token}")
    public Integer update(@RequestBody User user, @PathVariable("id") Integer id, @PathVariable("token") String token){
        User user1 = userMapper.selectById(id);
        //如果没查到，直接返回空值
        if(user == null)
            return null;
        if(!user.getToken().equals(token)){
            return null;
        }
        int i = userMapper.updateById(user);
        System.out.println(i);
        return i;
    }

    @GetMapping("/findbyid/{id}/{token}")
    public User findbyid(@PathVariable("id") Integer id, @PathVariable("token") String token) throws Exception {
            User user = userMapper.selectById(id);
            //如果没查到，直接返回空值
            if(user == null)
                return null;
            if(user.getToken().equals(token)){
                return user;
            }
            else{
                return null;
            }
    }

    @GetMapping("/findbyidnotoken/{id}")
    public User findbyidnotoken(@PathVariable("id") Integer id) throws Exception {
        User user = userMapper.selectById(id);
        return user;
    }

    @PostMapping("/uploadavatar")
    public String uploadavatar(@RequestBody Map<String, String> param) throws IOException {
        String token = param.get("token");
        String id = param.get("id");
        User user = userMapper.selectById(id);
        //如果没查到，直接返回空值
        if(user == null)
            return null;
        if(!user.getToken().equals(token)) {
            return null;
        }
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

    @GetMapping("/getclassmatelist/{id}/{token}")
     public List<User> getClassmateList(@PathVariable("id") Integer id,@PathVariable("token") String token){
        User user = userMapper.selectById(id);
        //如果没查到，直接返回空值
        if(user == null)
            return null;
        if(user.getToken().equals(token)){
            HashMap<String, Object> map = new HashMap<>();
            map.put("class1", user.getClass1());
            map.put("subject", user.getSubject());
            List<User> list = userMapper.selectByMap(map);
            return list;
        }
        else{
            return null;
        }
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

    //查询用户是否有发布活动的权限
    @GetMapping("/haveauthority/{id}/{token}")
    public Integer haveauthority(@PathVariable("id") Integer id, @PathVariable("token") String token) throws Exception {
        User user = userMapper.selectById(id);
        //如果没查到，直接返回空值
        if(user == null)
            return null;
        if(user.getToken().equals(token)){
            return user.getHaveauthority();
        }
        else{
            return null;
        }
    }




}

