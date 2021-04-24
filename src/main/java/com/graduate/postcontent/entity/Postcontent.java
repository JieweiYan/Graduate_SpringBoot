package com.graduate.postcontent.entity;

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
 * @since 2021-04-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Postcontent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 发（回）帖人的id
     */
    private Integer userid;

    /**
     * 发（回）帖人的名字
     */
    private String username;

    /**
     * 发（回）帖人头像url
     */
    private String useravatar;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 回复数
     */
    private Integer reply;

    /**
     * 发（回）帖时间
     */
    private Date time;

    /**
     * 浏览量
     */
    private Integer view;

    /**
     * 是否置顶
     */
    private Integer hometop;

    /**
     * 是否加精
     */
    private Integer postboutique;

    /**
     * 记录主贴id
     */
    private Integer mainpostid;

    /**
     * 如果是主贴，记录最后回帖时间，方便排序
     */
    private Date lastpost;


    //记录是否为主贴
    private Integer ismainpost;


}
