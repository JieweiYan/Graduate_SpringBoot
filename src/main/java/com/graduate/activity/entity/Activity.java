package com.graduate.activity.entity;

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
 * @since 2021-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 发起人id
     */
    private Integer userid;

    /**
     * 发起人名字
     */
    private String username;

    /**
     * 发起人头像url
     */
    private String useravatar;

    /**
     * 活动名称
     */
    private String activityname;

    /**
     * 可参加人数
     */
    private Integer participantsnum;

    /**
     * 已参加人数
     */
    private Integer participantednum;

    /**
     * 报名截止时间
     */
    private Date deadline;



    /**
     * 活动内容
     */
    private String content;

    /**
     * 活动开始时间
     */
    private Date starttime;

    private String url;


}
