package com.graduate.classalbum.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.graduate.classalbum.entity.Classalbum;
import com.graduate.classalbum.mapper.ClassalbumMapper;
import com.graduate.handler.AliyunOSSUtil;
import com.graduate.personalbum.entity.Personalbum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author YanJiewei
 * @since 2021-04-20
 */
@RestController
@RequestMapping("/classalbum")
public class ClassalbumController {
    @Autowired
    private ClassalbumMapper classalbumMapper;

    @PostMapping("/uploadalbum/{class1}/{subject}/{startyear}")
    public String uploadalbum(@RequestParam("avatar") MultipartFile multfile, @PathVariable("class1") String class1,
                              @PathVariable("subject") String subject, @PathVariable("startyear") String startyear) throws IOException {
        System.out.println(subject+startyear+class1);
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
        Classalbum classalbum = new Classalbum();
        classalbum.setClass1(class1);
        classalbum.setSubject(subject);
        classalbum.setStartyear(startyear);
        classalbum.setUrl(url);
        classalbum.setYearmonth(yearmonth);
        classalbumMapper.insert(classalbum);
        return "true";
    }

    @GetMapping("/findallpic/{class1}/{subject}/{startyear}")
    public String findballpic(@PathVariable("class1") String class1,
                              @PathVariable("subject") String subject, @PathVariable("startyear") String startyear){
        //查出对应用户id的照片，并且按照时间排序
        QueryWrapper<Classalbum> wrapper = new QueryWrapper<>();
        wrapper.eq("class1", class1);
        wrapper.eq("subject", subject);
        wrapper.eq("startyear", startyear);
        wrapper.orderByDesc("create_time");
        List<Classalbum> list= classalbumMapper.selectList(wrapper);
        //构建一个套娃列表存放查询出的照片
        List<List<Classalbum>> res = new ArrayList<>();
        if(list.size() == 0){
            return JSON.toJSONString(res);
        }
        int i = 0;
        for( ; ; ) {
            //每次必须新建list存放，因为java list.add是引用
            List<Classalbum> l = new ArrayList<>();
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
        return JSON.toJSONString(res);
    }


}

