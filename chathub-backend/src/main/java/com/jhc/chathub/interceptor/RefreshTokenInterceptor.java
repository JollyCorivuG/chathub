package com.jhc.chathub.interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.jhc.chathub.common.constants.RedisConstant;
import com.jhc.chathub.pojo.dto.UserDTO;
import com.jhc.chathub.utils.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RefreshTokenInterceptor implements HandlerInterceptor {
    private final StringRedisTemplate stringRedisTemplate;
    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        // 1.获取请求头中的token
        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)) {
            return true;
        }

        // 2.基于token从redis中获取用户信息
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(RedisConstant.USER_TOKEN_KEY + token);
        if (userMap.isEmpty()) {
            return true;
        }

        // 3.保存到ThreadLocal中
        UserHolder.saveUser(BeanUtil.fillBeanWithMap(userMap, new UserDTO(), false));

        // 4.刷新token的过期时间
        stringRedisTemplate.expire(RedisConstant.USER_TOKEN_KEY + token, RedisConstant.USER_TOKEN_KEY_TTL, TimeUnit.MINUTES);

        // 5.放行
        return true;
    }
}
