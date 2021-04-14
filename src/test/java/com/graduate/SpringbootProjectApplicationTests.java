package com.graduate;


import com.graduate.justtest.entity.Justtest;
import com.graduate.justtest.mapper.JusttestMapper;
import com.graduate.user.entity.User;
import com.graduate.user.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootProjectApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JusttestMapper justtestMapper;

    @Test
    void contextLoads() {
        userMapper.selectById(1);
    }
//
    @Test
    void saveTest(){
        Justtest justtest = new Justtest();
        justtest.setName("yjw");
        justtest.setAddress("ncdx");
        justtestMapper.insert(justtest);
    }

}
