package com.graduate;


import com.graduate.handler.AliyunOSSUtil;
import com.graduate.justtest.entity.Justtest;
import com.graduate.justtest.mapper.JusttestMapper;
import com.graduate.user.entity.User;
import com.graduate.user.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
class SpringbootProjectApplicationTests {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JusttestMapper justtestMapper;


//
    @Test
    void saveTest(){
        Justtest justtest = new Justtest();
        justtest.setName("yjw");
        justtest.setAddress("ncdx");
        justtestMapper.insert(justtest);
    }

    @Test
    void testUpload() {
        File file = new File("C:\\Users\\jiewe\\Desktop\\test.jpg");
        AliyunOSSUtil aliyunOSSUtil = new AliyunOSSUtil();
        String url = aliyunOSSUtil.upLoad(file);
        System.out.println(url);
    }

}
