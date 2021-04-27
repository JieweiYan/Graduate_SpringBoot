package com.graduate.chatroommassage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author YanJiewei
 * @since 2021-04-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Chatroommassage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 回信息人的id
     */
    private Integer userid;

    /**
     * 名字
     */
    private String username;

    /**
     * 头像url
     */
    private String useravatar;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息时间
     */
    private Date time;

    /**
     * 聊天室的班级
     */
    private String class1;

    /**
     * 聊天室的专业
     */
    private String subject;

    /**
     * 聊天室的入学年份
     */
    private String startyear;


}
