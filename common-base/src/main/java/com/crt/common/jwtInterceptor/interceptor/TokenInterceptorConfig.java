package com.crt.common.jwtInterceptor.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 自定义拦截器配置
 */
@Configuration
public class TokenInterceptorConfig implements WebMvcConfigurer {

    @Resource
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 自定义拦截器，添加拦截路径和排除拦截路径
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**");
    }
}
