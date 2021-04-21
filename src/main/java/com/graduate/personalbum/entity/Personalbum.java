package com.graduate.personalbum.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2021-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Personalbum implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 该照片用户的id
     */
    private Integer userid;

    /**
     * 照片的URL
     */
    private String url;

    /**
     * 上传的年月
     */
    private String yearmonth;

    /**
     * 上传时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


}
