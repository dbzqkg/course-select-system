package com.lzh.config;


import com.lzh.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 1. 把咱们的保安大爷注册进来
        registry.addInterceptor(loginInterceptor)
                // 2. 拦截所有请求 (保安大爷站在总大门)
                .addPathPatterns("/**")
                // 3. 配置 VIP 白名单 (这些路径不需要查 Token，直接放行)
                .excludePathPatterns(
                        "/api/student/login",    // 登录接口必须放行，不然怎么拿 Token？
                        "/swagger-ui/**",    // 放行 Swagger 网页的各种 HTML、JS 静态资源
                        "/v3/api-docs/**",   // 放行 Swagger 读取咱们接口信息的内部请求
                        "/error",             // 放行 Spring Boot 默认的错误路径
                        "/api/student/register"
                );
    }
}
