package com.graduate.user.controller;


import com.graduate.handler.AliyunOSSUtil;
import com.graduate.user.entity.User;
import com.graduate.user.mapper.UserMapper;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
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
    public String uploadavatar(@RequestBody String image) throws IOException {
        //base64先保存到本地
        String base64Image = image.split("%2C", 2)[1];
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
        return url;
    }


}

