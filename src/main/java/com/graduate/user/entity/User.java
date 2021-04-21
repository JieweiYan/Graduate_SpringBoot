package com.graduate.user.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author YanJiewei
 * @since 2021-03-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 电话号码
     */
    private String telnum;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String name;

    /**
     * 性别
     */
    private String sex;

    /**
     * 出生日期
     */
    private LocalDate birthday;

    /**
     * 专业
     */
    private String subject;

    /**
     * 入学年份
     */
    private String startyear;

    /**
     * 班级
     */
    private String class1;

    /**
     * 现居地
     */
    private String location;

    /**
     * 职业
     */
    private String profession;

    /**
     * 微信号
     */
    private String wechatnum;

    /**
     * 一句话介绍
     */
    private String introduce;

    /**
     * 个人简介
     */
    private String profile;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


    /**
     * 头像地址
     */
    private String avatar;

    //盐
    private String salt;

}
