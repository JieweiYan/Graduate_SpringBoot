package com.graduate.notice.service.impl;

import com.graduate.notice.entity.Notice;
import com.graduate.notice.mapper.NoticeMapper;
import com.graduate.notice.service.NoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author YanJiewei
 * @since 2021-04-27
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

}
