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
        System.out.println(mainpostid);
        System.out.println("hhhhhh");
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
        postcontentMapper.selectList(wrapper);
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
    @GetMapping("/findpostbyid/{postid}/{pageid}")
    public Map<String, Object> findpostbyid(@PathVariable("postid") Integer postid,@PathVariable("pageid") Integer pageid)  {
        Map<String, Object> map = new HashMap<>();
        Postcontent mainpost = postcontentMapper.selectById(postid);
        map.put("mainposttitle", mainpost.getTitle());
        //每次查询，浏览量+1
        mainpost.setView(mainpost.getView() + 1);
        postcontentMapper.updateById(mainpost);
        //查出对应页回帖内容
        QueryWrapper<Postcontent> wrapper = new QueryWrapper<>();
        wrapper.eq("mainpostid", postid);
        wrapper.orderByAsc("time");
        if(pageid == 1){
            Page<Postcontent> page = new Page<>(pageid, 11);
            Page<Postcontent> replylist = postcontentMapper.selectPage(page, wrapper);
            //再查出主贴内容
            QueryWrapper<Postcontent> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("id", postid);
            wrapper1.eq("mainpostid", 0);
            List<Postcontent> mainpostlist = postcontentMapper.selectList(wrapper1);
            //再拼接起来
            map.put("mainpost", mainpostlist);
            map.put("replypostlist", replylist);
            return map;
        }
        else{
            //因为不是第一页，所以不需要去查主贴了
            Page<Postcontent> page = new Page<>(pageid, 11);
            Page<Postcontent> replylist = postcontentMapper.selectPage(page, wrapper);
            map.put("replypostlist", replylist);
            return map;
        }
    }

    //根据id查看回帖条数
    //这里拿到的postid是主贴id
    @GetMapping("/findpostnumbyid/{postid}")
    public Integer findpostnumbyid(@PathVariable("postid") Integer postid)  {
        QueryWrapper<Postcontent> wrapper = new QueryWrapper<>();
        wrapper.eq("mainpostid", postid);
        return postcontentMapper.selectCount(wrapper) + 1;
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

//    //搜索帖子
    @PostMapping("/searchpost/{pageid}")
    public Page<Postcontent> searchpost(@RequestBody Map<String, String> map, @PathVariable("pageid") Integer pageid)  {
        //参数1:当前页
        //参数2:页面大小
        String tag = map.get("tag");
        Page<Postcontent> page = new Page<>(pageid, 10);
        QueryWrapper<Postcontent> wrapper = new QueryWrapper<>();
        wrapper.like("title", tag);
        wrapper.eq("mainpostid", 0);
        wrapper.orderByDesc("lastpost");
        return postcontentMapper.selectPage(page, wrapper);
    }

    //搜索符合关键字的帖子总条数
    @PostMapping("/searchpostnum")
    public Integer searchpostnum(@RequestBody Map<String, String> map)  {
        String tag = map.get("tag");
        QueryWrapper<Postcontent> wrapper = new QueryWrapper<>();
        wrapper.eq("mainpostid", 0);
        wrapper.like("title", tag);
        return postcontentMapper.selectCount(wrapper);
    }

    //搜索用户
    @PostMapping("/searchpeople/{pageid}")
    public Page<User> searchpeople(@RequestBody User user, @PathVariable("pageid") Integer pageid)  {
        //参数1:当前页
        //参数2:页面大小
        Page<User> page = new Page<>(pageid, 10);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if(user.getClass1() != "")
            wrapper.eq("class1", user.getClass1());
        if(user.getSubject() != "")
            wrapper.eq("subject", user.getSubject());
        if(user.getStartyear() != "")
            wrapper.eq("startyear", user.getStartyear());
        if(user.getName() != "")
            wrapper.like("name", user.getName());
        return userMapper.selectPage(page, wrapper);
    }

    //搜索用户,返回符合条件用户数
    @PostMapping("/searchpeoplenum")
    public Integer searchpeoplenum(@RequestBody User user)  {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if(user.getClass1() != "")
            wrapper.eq("class1", user.getClass1());
        if(user.getSubject() != "")
            wrapper.eq("subject", user.getSubject());
        if(user.getStartyear() != "")
            wrapper.eq("startyear", user.getStartyear());
        if(user.getName() != "")
            wrapper.like("name", user.getName());
        return userMapper.selectCount(wrapper);
    }

    //    //搜索用户发过的帖子帖子
    @GetMapping("/searchpost/{userid}/{pageid}")
    public Page<Postcontent> searchpostbyuserid(@PathVariable("userid") Integer userid , @PathVariable("pageid") Integer pageid)  {
        //参数1:当前页
        //参数2:页面大小
        Page<Postcontent> page = new Page<>(pageid, 10);
        QueryWrapper<Postcontent> wrapper = new QueryWrapper<>();
        wrapper.like("userid", userid);
        wrapper.eq("mainpostid", 0);
        wrapper.orderByDesc("lastpost");
        return postcontentMapper.selectPage(page, wrapper);
    }

    //搜索符合关键字的帖子总条数
    @GetMapping("/searchpostnumbyuserid/{userid}")
    public Integer searchpostnum(@PathVariable("userid") Integer userid)  {
        QueryWrapper<Postcontent> wrapper = new QueryWrapper<>();
        wrapper.eq("mainpostid", 0);
        wrapper.like("userid", userid);
        return postcontentMapper.selectCount(wrapper);
    }


    @PostMapping("/delete/{postid}")
    public Integer delete(@PathVariable("postid") Integer postid)  {
        return postcontentMapper.deleteById(postid);
    }

    @GetMapping("/getallpost")
    public List<Postcontent> getAllPost(){
        QueryWrapper<Postcontent> wrapper = new QueryWrapper<>();
        wrapper.eq("mainpostid", 0);
        wrapper.orderByDesc("lastpost");
        return postcontentMapper.selectList(wrapper);
    }

}
