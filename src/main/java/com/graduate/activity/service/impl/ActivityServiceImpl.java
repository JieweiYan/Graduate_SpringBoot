package com.graduate.activity.service.impl;

import com.graduate.activity.entity.Activity;
import com.graduate.activity.mapper.ActivityMapper;
import com.graduate.activity.service.ActivityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author YanJiewei
 * @since 2021-04-26
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {

}
