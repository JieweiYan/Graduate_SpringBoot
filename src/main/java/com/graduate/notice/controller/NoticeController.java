package com.graduate.notice.controller;


import com.graduate.activity.entity.Activity;
import com.graduate.notice.entity.Notice;
import com.graduate.notice.mapper.NoticeMapper;
import com.graduate.postcontent.entity.Postcontent;
import com.graduate.user.entity.User;
import com.graduate.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author YanJiewei
 * @since 2021-04-27
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NoticeMapper noticeMapper;



    //更新公告接口
    @PostMapping("/updatenotice/{id}/{token}")
    public Integer updatenotice(@PathVariable("id") Integer id, @PathVariable("token") String token, @RequestBody Notice notice) {
        User user = userMapper.selectById(id);
        //如果没查到，直接返回空值
        if(user == null)
            return null;
        if(user.getToken().equals(token)){
            notice.setId(1);
            if(noticeMapper.selectById(1)==null)
                return noticeMapper.insert(notice);
            else
                return noticeMapper.updateById(notice);
        }
        else{
            return null;
        }
    }

    //查看公告
    @GetMapping("/findnotice")
    public Notice findactivitybyid()  {
        return noticeMapper.selectById(1);
    }

}

