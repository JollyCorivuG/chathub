package bupt.edu.jhc.chathub.common.interceptor;

import bupt.edu.jhc.chathub.common.utils.context.RequestInfo;
import bupt.edu.jhc.chathub.common.domain.constants.MDCKeys;
import bupt.edu.jhc.chathub.common.domain.constants.RedisConstants;
import bupt.edu.jhc.chathub.common.utils.context.RequestHolder;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.MDC;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 刷新 token 拦截器
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/16
 */
@Component
public class RefreshTokenInterceptor implements HandlerInterceptor {
    private final StringRedisTemplate stringRedisTemplate;

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    private static final String TOKEN_KET = "token";

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        // 1.获取请求头中的 token 或者 url 中的 token
        String token = request.getHeader(TOKEN_KET);
        if (StrUtil.isBlank(token)) {
            token = request.getParameter(TOKEN_KET);
        }
        if (StrUtil.isBlank(token)) {
            return true;
        }

        // 2.基于 token 从 redis 中获取请求信息
        Map<Object, Object> requestInfoMap = stringRedisTemplate.opsForHash().entries(RedisConstants.USER_TOKEN_KEY + token);
        if (requestInfoMap.isEmpty()) {
            stringRedisTemplate.opsForSet().remove(RedisConstants.ONLINE_USER_KEY, token);
            return true;
        }

        // 3.将 uid 记录到日志
        RequestInfo requestInfo = BeanUtil.fillBeanWithMap(requestInfoMap, new RequestInfo(), false);
        MDC.put(MDCKeys.UID, String.valueOf(requestInfo.getUid()));

        // 4.保存到 ThreadLocal 中
        RequestHolder.save(requestInfo);

        // 5.刷新 token 的过期时间
        stringRedisTemplate.expire(RedisConstants.USER_TOKEN_KEY + token, RedisConstants.USER_TOKEN_KEY_TTL, TimeUnit.MINUTES);

        // 6.放行
        return true;
    }
}
