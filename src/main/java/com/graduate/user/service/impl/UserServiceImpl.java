package com.graduate.user.service.impl;

import com.graduate.user.entity.User;
import com.graduate.user.mapper.UserMapper;
import com.graduate.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author YanJiewei
 * @since 2021-03-30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
