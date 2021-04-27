package com.graduate.activity.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.graduate.activity.entity.Activity;
import com.graduate.activity.mapper.ActivityMapper;
import com.graduate.participateactivity.entity.Participateactivity;
import com.graduate.participateactivity.mapper.ParticipateactivityMapper;
import com.graduate.postcontent.entity.Postcontent;
import com.graduate.user.entity.User;
import com.graduate.user.mapper.UserMapper;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author YanJiewei
 * @since 2021-04-26
 */
@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ParticipateactivityMapper participateactivityMapper;

    //发布新活动接口
    @PostMapping("/postactivity/{id}/{token}")
    public String post(@PathVariable("id") Integer id, @PathVariable("token") String token, @RequestBody Activity activity) {
        System.out.println(id);
        User user = userMapper.selectById(id);
        //如果没查到，直接返回空值
        if(user == null)
            return null;
        if(user.getToken().equals(token)){
            activity.setUserid(user.getId());
            activity.setUseravatar(user.getAvatar());
            activity.setUsername(user.getName());
            activity.setParticipantednum(0);
            activityMapper.insert(activity);
            return "200";
        }
        else{
            return null;
        }
    }

    //查找所有活动供论坛首页展示
    @GetMapping("/findallactivity/{pageid}")
    public Page<Activity> findallactivity(@PathVariable("pageid") Integer pageid)  {
        //参数1:当前页
        //参数2:页面大小
        Page<Activity> page = new Page<>(pageid, 12);
        QueryWrapper<Activity> wrapper = new QueryWrapper<>();
        wrapper.isNotNull("id");
        wrapper.orderByDesc("deadline");
        return activityMapper.selectPage(page, wrapper);
    }

    //查询活动总条数
    @GetMapping("/findallactivitynum")
    public Integer findallactivitynum()  {
        QueryWrapper<Activity> wrapper = new QueryWrapper<>();
        wrapper.isNotNull("id");
        return activityMapper.selectCount(wrapper);
    }

    //根据活动id查找活动
    @GetMapping("/findactivitybyid/{activityid}")
    public Activity findactivitybyid(@PathVariable("activityid") Integer activityid)  {
        return activityMapper.selectById(activityid);
    }

    //查询用户是否已参加活动
    @GetMapping("/finduserstatu/{activityid}/{userid}")
    public Integer finduserstatu(@PathVariable("activityid") Integer activityid, @PathVariable("userid") Integer userid)  {
        QueryWrapper<Participateactivity> wrapper = new QueryWrapper<>();
        wrapper.eq("activityid", activityid);
        wrapper.eq("userid", userid);
        if(participateactivityMapper.selectCount(wrapper) == 0)
            return 0;
        else
            return 1;
    }

    //参加活动
    @PostMapping("/joinactivity/{activityid}/{userid}")
    public Integer joinactivity(@PathVariable("activityid") Integer activityid, @PathVariable("userid") Integer userid)  {
        Participateactivity participateactivity = new Participateactivity();
        participateactivity.setActivityid(activityid);
        participateactivity.setUserid(userid);
        Activity activity = activityMapper.selectById(activityid);
        activity.setParticipantednum(activity.getParticipantednum() + 1);
        activityMapper.updateById(activity);
        return participateactivityMapper.insert(participateactivity);
    }

    //退出活动
    @PostMapping("/exitactivity/{activityid}/{userid}")
    public Integer exitactivity(@PathVariable("activityid") Integer activityid, @PathVariable("userid") Integer userid)  {
        QueryWrapper<Participateactivity> wrapper = new QueryWrapper<>();
        wrapper.eq("activityid", activityid);
        wrapper.eq("userid", userid);
        Activity activity = activityMapper.selectById(activityid);
        activity.setParticipantednum(activity.getParticipantednum() - 1);
        activityMapper.updateById(activity);
        return participateactivityMapper.delete(wrapper);
    }

    //导出参加活动人员的excel
    @GetMapping("/exportexcel/{activityid}")
    public void exportexcel(@PathVariable("activityid") Integer activityid,HttpServletResponse response) throws IOException {
        Activity activity = activityMapper.selectById(activityid);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("人员信息");
        // 设置要导出的文件的名字
        String fileName = activity.getActivityname() + "人员信息表.xls";
        System.out.println(fileName);
        // 新增数据行，并且设置单元格数据
        int rowNum = 1;

        // headers表示excel表中第一行的表头 在excel表中添加表头
        String[] headers = { "姓名", "性别", "专业", "入学年份", "班级", "电话号码", "微信号"};
        HSSFRow row = sheet.createRow(0);
        for(int i=0;i<headers.length;i++){
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        QueryWrapper<Participateactivity> wrapper = new QueryWrapper<>();
        wrapper.eq("activityid", activityid);
        List<Participateactivity> list = participateactivityMapper.selectList(wrapper);

        //在表中存放查询到的数据放入对应的列
        for (Participateactivity item : list) {
            User user = userMapper.selectById(item.getUserid());
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(user.getName());
            row1.createCell(1).setCellValue(user.getSex());
            row1.createCell(2).setCellValue(user.getSubject());
            row1.createCell(3).setCellValue(user.getStartyear());
            row1.createCell(4).setCellValue(user.getClass1());
            row1.createCell(5).setCellValue(user.getTelnum());
            row1.createCell(6).setCellValue(user.getWechatnum());
            rowNum++;
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
        response.setContentType("application/vnd.ms-excel;charset=gb2312");
        response.flushBuffer();
        workbook.write(response.getOutputStream());
    }



}

