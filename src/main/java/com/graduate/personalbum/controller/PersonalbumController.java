package com.graduate.personalbum.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.graduate.handler.AliyunOSSUtil;
import com.graduate.personalbum.entity.Personalbum;
import com.graduate.personalbum.mapper.PersonalbumMapper;
import com.graduate.user.entity.User;
import com.graduate.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import static com.graduate.user.Tools.judgeAuthority;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author YanJiewei
 * @since 2021-04-20
 */
@RestController
@RequestMapping("/personalbum")
public class PersonalbumController {

    @Autowired
    private PersonalbumMapper personalbumMapper;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/uploadalbum/{id}")
    public String uploadalbum(@RequestParam("avatar") MultipartFile multfile, @PathVariable("id") Integer id) throws Exception {
        // 获取文件名
        String fileName = multfile.getOriginalFilename();
        // 获取文件后缀
        String prefix=fileName.substring(fileName.lastIndexOf("."));
        // 用uuid作为文件名，防止生成的临时文件重复
        File file = File.createTempFile(UUID.randomUUID().toString(), prefix);
        // MultipartFile to File
        multfile.transferTo(file);
        //然后上传本地文件到阿里云oss
        AliyunOSSUtil aliyunOSSUtil = new AliyunOSSUtil();
        String url = aliyunOSSUtil.upLoad(file);
        System.out.println(url);
        Calendar cal = Calendar.getInstance();
        Integer year = cal .get(Calendar.YEAR);
        Integer month = cal .get(Calendar.MONTH) + 1;
        String yearmonth = year.toString()+"年"+month.toString()+"月";
        //插入数据库
        Personalbum personalbum = new Personalbum();
        personalbum.setUserid(id);
        personalbum.setUrl(url);
        personalbum.setYearmonth(yearmonth);
        personalbumMapper.insert(personalbum);
        return "true";
    }

    @GetMapping("/findbyid/{userid}")
    public String findbyuserid(@PathVariable("userid") Integer userid){
        //查出对应用户id的照片，并且按照时间排序
        QueryWrapper<Personalbum> wrapper = new QueryWrapper<>();
        wrapper.eq("userid", userid);
        wrapper.orderByDesc("create_time");
        List<Personalbum> list= personalbumMapper.selectList(wrapper);
        //构建一个套娃列表存放查询出的照片
        List<List<Personalbum>> res = new ArrayList<>();
        if(list.size() == 0){
            return JSON.toJSONString(res);
        }
        int i = 0;
        for( ; ; ) {
            //每次必须新建list存放，因为java list.add是引用
            List<Personalbum> l = new ArrayList<>();
            l.add(list.get(i));
            i++;
            for (; i < list.size(); i++) {
                if (list.get(i - 1).getYearmonth().equals(list.get(i).getYearmonth())) {
                    l.add(list.get(i));
                } else {
                    break;
                }
            }
            res.add(l);
            if(i == list.size())
                break;
        }
        System.out.println(JSON.toJSONString(res));
        return JSON.toJSONString(res);
    }

    @PostMapping("/deletepic/{id}/{userid}/{token}")
    public String deletepic(@PathVariable("id") Integer id, @PathVariable("userid") Integer userid, @PathVariable("token") String token){
        User user = userMapper.selectById(userid);
        //如果没查到，直接返回空值
        if(user == null)
            return null;
        if(user.getToken().equals(token)){
            personalbumMapper.deleteById(id);
            return "200";
        }
        else{
            return null;
        }
    }

}

