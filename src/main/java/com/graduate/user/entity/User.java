package com.graduate.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.io.Serializable;
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
    private String startYear;

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
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;


}
