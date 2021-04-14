package com.graduate.user.controller;


import com.graduate.user.entity.User;
import com.graduate.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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




}

