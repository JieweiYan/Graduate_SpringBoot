package com.graduate.participateactivity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2021-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Participateactivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 参加活动的用户的id
     */
    private Integer userid;

    /**
     * 参加的活动id
     */
    private Integer activityid;


}
