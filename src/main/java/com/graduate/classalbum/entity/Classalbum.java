package com.graduate.classalbum.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
 * @since 2021-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Classalbum implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 照片url
     */
    private String url;

    /**
     * 班级相册的班级
     */
    private String class1;

    /**
     * 班级相册的专业
     */
    private String subject;

    /**
     * 班级相册的入学年份（用于确定照片属于哪个班）
     */
    private String startyear;

    /**
     * 上传时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 上传的年月
     */
    private String yearmonth;

}
