package com.graduate.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description: TODO
 * @author: scott
 * @date: 2021年03月20日 23:15
 */


@Configuration
@EnableTransactionManagement
@MapperScan({"com.graduate.user.mapper", "com.graduate.justtest.mapper", "com.graduate.classalbum.mapper",
        "com.graduate.personalbum.mapper", "com.graduate.postcontent.mapper"})
public class MyBatisPlusConfig {
    // 旧版
    @Bean
    public PaginationInterceptor paginationInterceptor() {

        return new PaginationInterceptor();
    }







}
