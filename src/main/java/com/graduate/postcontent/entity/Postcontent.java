package com.graduate.postcontent.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author YanJiewei
 * @since 2021-04-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Postcontent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Integer id;

    /**
     * 发帖人的id
     */
    private Integer userid;

    /**
     * 发帖人的名字
     */
    private String username;

    /**
     * 发帖人头像url
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
     * 发帖时间
     */
    private LocalDateTime time;

    /**
     * 浏览量
     */
    private Integer view;


}
