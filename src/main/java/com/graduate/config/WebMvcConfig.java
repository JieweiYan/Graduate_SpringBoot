package com.graduate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description: TODO
 * @author: scott
 * @date: 2021年04月14日 21:07
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("Http://localhost:8080")
        .allowedMethods("GET", "PUT", "POST", "OPTIONS", "DELETE")
        .allowCredentials(true)
        .maxAge(3600);
    }
}
