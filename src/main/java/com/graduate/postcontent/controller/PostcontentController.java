package com.graduate.postcontent.controller;


import com.graduate.classalbum.entity.Classalbum;
import com.graduate.handler.AliyunOSSUtil;
import com.graduate.postcontent.WangEditor;
import com.graduate.postcontent.mapper.PostcontentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author YanJiewei
 * @since 2021-04-22
 */
@RestController
@RequestMapping("/postcontent")
public class PostcontentController {
    @Autowired
    private PostcontentMapper postcontentMapper;

    //富文本编辑器上传图片接口
    @PostMapping("/uploadpic")
    public WangEditor uploadalbum(@RequestParam("myFile") MultipartFile multfile) throws IOException {
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
        WangEditor we = new WangEditor(url);
        return we;
    }

}

