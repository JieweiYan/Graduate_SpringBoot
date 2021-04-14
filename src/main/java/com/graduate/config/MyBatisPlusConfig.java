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
@MapperScan({"com.graduate.user.mapper", "com.graduate.justtest.mapper"})
public class MyBatisPlusConfig {
    // 旧版
    @Bean
    public PaginationInterceptor paginationInterceptor() {

        return new PaginationInterceptor();
    }

    //逻辑删除组件



    //解决跨域问题
//    @Bean
//    public WebMvcConfigurer corsConfigurer(){
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry){
//                registry.addMapping("/**")
//                        .allowedOrigins("*")
//                        .allowedMethods("GET", "PUT", "HEAD", "POST", "DELETE", "OPTIONS")
//                        .allowCredentials(false);
//            }
//        };
//    }


}
