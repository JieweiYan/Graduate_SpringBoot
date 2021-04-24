package com.graduate.postcontent.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.graduate.classalbum.entity.Classalbum;
import com.graduate.handler.AliyunOSSUtil;
import com.graduate.postcontent.WangEditor;
import com.graduate.postcontent.entity.Postcontent;
import com.graduate.postcontent.mapper.PostcontentMapper;
import com.graduate.user.entity.User;
import com.graduate.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

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

    @Autowired
    private UserMapper userMapper;

    //富文本编辑器上传图片接口
    @PostMapping("/uploadpic")
    public Map<String, Object> uploadalbum(@RequestParam("myFile") List<MultipartFile> multfile) throws IOException {
        String imgUrls[] = new String[multfile.size()];
        for(int i = 0; i < multfile.size(); i++){
            // 获取文件名
            String fileName = multfile.get(i).getOriginalFilename();
            // 获取文件后缀
            String prefix=fileName.substring(fileName.lastIndexOf("."));
            // 用uuid作为文件名，防止生成的临时文件重复
            File file = File.createTempFile(UUID.randomUUID().toString(), prefix);
            // MultipartFile to File
            multfile.get(i).transferTo(file);
            //然后上传本地文件到阿里云oss
            AliyunOSSUtil aliyunOSSUtil = new AliyunOSSUtil();
            String url = aliyunOSSUtil.upLoad(file);
            System.out.println(url);
            imgUrls[i] = url;
        }
        Map<String, Object> resultMap =new HashMap();
        resultMap.put("errno", 0);
        resultMap.put("data", imgUrls);
        return resultMap;
    }

    //发布新帖接口
    @PostMapping("/post/{id}/{token}")
    public String post(@PathVariable("id") Integer id, @PathVariable("token") String token, @RequestBody Postcontent postcontent) {
        User user = userMapper.selectById(id);
        //如果没查到，直接返回空值
        if(user == null)
            return null;
        if(user.getToken().equals(token)){
            postcontent.setUserid(user.getId());
            postcontent.setUsername(user.getName());
            postcontent.setUseravatar(user.getAvatar());
            postcontent.setReply(0);
            postcontent.setTime(new Date());
            postcontent.setView(0);
            postcontent.setMainpostid(0);
            postcontent.setLastpost(new Date());
            postcontentMapper.insert(postcontent);
            return "200";
        }
        else{
            return null;
        }
    }

    //回复帖子接口
    @PostMapping("/reply/{mainpostid}/{id}/{token}")
    public String reply(@PathVariable("mainpostid") Integer mainpostid, @PathVariable("id") Integer id, @PathVariable("token") String token, @RequestBody Postcontent postcontent) {
        User user = userMapper.selectById(id);
        //如果没查到，直接返回空值
        if(user == null)
            return null;
        if(user.getToken().equals(token)){
            Postcontent mainpost = postcontentMapper.selectById(mainpostid);
            //回复和浏览数都+1
            mainpost.setReply(mainpost.getReply() + 1);
            //更新最后回帖时间
            mainpost.setLastpost(new Date());
            postcontentMapper.updateById(mainpost);

            //设置插入的回复
            postcontent.setUserid(user.getId());
            postcontent.setUsername(user.getName());
            postcontent.setUseravatar(user.getAvatar());
            postcontent.setTime(new Date());
            postcontent.setMainpostid(mainpostid);
            postcontentMapper.insert(postcontent);
            return "200";
        }
        else{
            return null;
        }
    }

    //查找所有帖子供论坛首页展示
    @GetMapping("/findallpost/{pageid}")
    public Page<Postcontent> findallpost(@PathVariable("pageid") Integer pageid)  {
        //参数1:当前页
        //参数2:页面大小
        Page<Postcontent> page = new Page<>(pageid, 12);
        QueryWrapper<Postcontent> wrapper = new QueryWrapper<>();
        wrapper.eq("mainpostid", 0);
        wrapper.orderByDesc("lastpost");
        return postcontentMapper.selectPage(page, wrapper);
    }

    //查询帖子总条数
    @GetMapping("/findallpostnum")
    public Integer findallpostnum()  {
        QueryWrapper<Postcontent> wrapper = new QueryWrapper<>();
        wrapper.eq("mainpostid", 0);
        return postcontentMapper.selectCount(wrapper);
    }

    //根据id查看帖子
    //这里拿到的postid是主贴id
    @GetMapping("/findpostbyid/{postid}")
    public Map<String, Object> findpostbyid(@PathVariable("postid") Integer postid)  {
        Map<String, Object> map = new HashMap<>();
        Postcontent mainpost = postcontentMapper.selectById(postid);
        map.put("mainposttitle", mainpost.getTitle());
        //每次查询，浏览量+1
        mainpost.setView(mainpost.getView() + 1);
        postcontentMapper.updateById(mainpost);
        //查出所有回帖内容
        QueryWrapper<Postcontent> wrapper = new QueryWrapper<>();
        wrapper.eq("mainpostid", postid);
        wrapper.orderByAsc("time");
        List<Postcontent> replylist = postcontentMapper.selectList(wrapper);
        //再查出主贴内容
        QueryWrapper<Postcontent> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("id", postid);
        wrapper1.eq("mainpostid", 0);
        List<Postcontent> mainpostlist = postcontentMapper.selectList(wrapper1);
        //再拼接起来
        mainpostlist.addAll(replylist);
        map.put("replypostlist", mainpostlist);
        return map;
    }

    //本周热榜
    @GetMapping("/weekhotlist")
    public List<Postcontent> weekhotlist()  {
        QueryWrapper<Postcontent> wrapper = new QueryWrapper<>();
        wrapper.eq("mainpostid", 0);
        wrapper.orderByDesc("view");
        wrapper.last("limit 8");
        //查找近一周的热帖
        Calendar c = Calendar.getInstance();
        c.setTime( new  Date());
        c.add(Calendar.DATE, -  7 );
        Date date = c.getTime();
        wrapper.gt("lastpost", date);
        List<Postcontent> hotpostlist = postcontentMapper.selectList(wrapper);
        return hotpostlist;
    }

    //今日热榜
    @GetMapping("/todayhotlist")
    public List<Postcontent> todayhotlist()  {
        QueryWrapper<Postcontent> wrapper = new QueryWrapper<>();
        wrapper.eq("mainpostid", 0);
        wrapper.orderByDesc("view");
        wrapper.last("limit 8");
        //查找近一周的热帖
        Calendar c = Calendar.getInstance();
        c.setTime( new  Date());
        c.add(Calendar.DATE, -  1 );
        Date date = c.getTime();
        wrapper.gt("lastpost", date);
        List<Postcontent> hotpostlist = postcontentMapper.selectList(wrapper);
        return hotpostlist;
    }



}
