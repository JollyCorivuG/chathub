package com.jhc.chathub.config;

import com.jhc.chathub.interceptor.CheckTokenInterceptor;
import com.jhc.chathub.interceptor.RefreshTokenInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 刷新token的拦截器
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate)).addPathPatterns("/**").order(0);

        // 需要校验token的拦截器
        registry.addInterceptor(new CheckTokenInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/doc.html/**",
                        "/webjars/**",
                        "/v3/api-docs/swagger-config/**",
                        "/favicon.ico",
                        "/v3/api-docs/**",
                        "/upload/**",
                        "/users/login",
                        "/users/register",
                        "/users/phone_code/**",
                        "/users/phone_login"
                ).order(1);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations(
                        "file:upload/user_avatar/"
                );
    }
}
