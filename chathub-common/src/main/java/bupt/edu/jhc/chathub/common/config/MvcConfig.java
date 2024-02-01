package bupt.edu.jhc.chathub.common.config;

import bupt.edu.jhc.chathub.common.interceptor.CheckTokenInterceptor;
import bupt.edu.jhc.chathub.common.interceptor.RefreshTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @Description: Mvc 配置
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/16
 */
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
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/upload/**",
                        "/api/users/login",
                        "/api/users/register",
                        "/api/users/phone_code/**",
                        "/api/users/phone_login",
                        "/api/sse/unsubscribe"
                ).order(1);
    }
}