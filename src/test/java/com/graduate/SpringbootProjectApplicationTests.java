package com.graduate;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.graduate.handler.AliyunOSSUtil;
import com.graduate.justtest.entity.Justtest;
import com.graduate.justtest.mapper.JusttestMapper;
import com.graduate.personalbum.entity.Personalbum;
import com.graduate.personalbum.mapper.PersonalbumMapper;
import com.graduate.user.entity.User;
import com.graduate.user.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@SpringBootTest
class SpringbootProjectApplicationTests {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JusttestMapper justtestMapper;

    @Autowired
    private PersonalbumMapper personalbumMapper;


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

    @Test
    void selectalbum(){
        int id = 12;
        QueryWrapper<Personalbum> wrapper = new QueryWrapper<>();
        wrapper.eq("userid", id);
        wrapper.orderByDesc("create_time");
        List<Personalbum> list= personalbumMapper.selectList(wrapper);
        System.out.println(list.size());
        List<List<Personalbum>> res = new ArrayList<>();
        if(list.size() == 0){
            return;
        }
        int i = 0;
        for( ; ; ) {
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
        return;
    }
}
