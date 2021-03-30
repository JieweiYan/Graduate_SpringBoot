package com.graduate.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Description: TODO
 * @author: scott
 * @date: 2021年03月20日 23:15
 */


@Configuration
@EnableTransactionManagement
@MapperScan("com.graduate.user.mapper")
public class MyBatisPlusConfig {
    // 旧版
    @Bean
    public PaginationInterceptor paginationInterceptor() {

        return new PaginationInterceptor();
    }

    //逻辑删除组件
    @Bean
    public ISqlInjector sqlInjector(){
        return new LogicSqlInjector();
    }



}
