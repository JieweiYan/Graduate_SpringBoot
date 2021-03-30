package com.graduate;

import com.graduate.user.entity.User;
import com.graduate.user.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootProjectApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        userMapper.selectById(1);
    }

}
