package com.graduate.chatroommassage.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.graduate.chatroommassage.entity.Chatroommassage;
import com.graduate.chatroommassage.mapper.ChatroommassageMapper;
import com.graduate.postcontent.entity.Postcontent;
import com.graduate.user.entity.User;
import com.graduate.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author YanJiewei
 * @since 2021-04-25
 */
@RestController
@RequestMapping("/chatroommassage")
public class ChatroommassageController {
    @Autowired
    private ChatroommassageMapper chatroommassageMapper;

    @Autowired
    private UserMapper userMapper;

    //班级聊天室回复接口
    @PostMapping("/reply/{id}/{token}")
    public String reply(@PathVariable("id") Integer id, @PathVariable("token") String token, @RequestBody Chatroommassage chatroommassage) {
        User user = userMapper.selectById(id);
        //如果没查到，直接返回空值
        if(user == null)
            return null;
        if(user.getToken().equals(token)){
            chatroommassage.setUserid(user.getId());
            chatroommassage.setUsername(user.getName());
            chatroommassage.setUseravatar(user.getAvatar());
            chatroommassage.setTime(new Date());
            chatroommassage.setClass1(user.getClass1());
            chatroommassage.setSubject(user.getSubject());
            chatroommassage.setStartyear(user.getStartyear());
            chatroommassageMapper.insert(chatroommassage);
            return "200";
        }
        else{
            return null;
        }
    }

    //班级聊天室查询所有消息接口
    @GetMapping("/getmassage/{id}/{pageid}")
    public Page<Chatroommassage> getmassage(@PathVariable("id") Integer id, @PathVariable("pageid") Integer pageid) {
        User user = userMapper.selectById(id);
        QueryWrapper<Chatroommassage> wrapper = new QueryWrapper<>();
        wrapper.eq("class1", user.getClass1());
        wrapper.eq("subject", user.getSubject());
        wrapper.eq("startyear", user.getStartyear());
        wrapper.orderByDesc("time");
        Page<Chatroommassage> page = new Page<>(pageid, 12);
        return chatroommassageMapper.selectPage(page, wrapper);
    }

    //班级聊天室查询所有消息数量接口
    @GetMapping("/getmassagenum/{id}")
    public Integer getmassagenum(@PathVariable("id") Integer id) {
        User user = userMapper.selectById(id);
        QueryWrapper<Chatroommassage> wrapper = new QueryWrapper<>();
        wrapper.eq("class1", user.getClass1());
        wrapper.eq("subject", user.getSubject());
        wrapper.eq("startyear", user.getStartyear());
        return chatroommassageMapper.selectCount(wrapper);

    }
}

