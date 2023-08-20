package com.jhc.chathub.aop;

import cn.hutool.core.lang.UUID;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/19
 */
@Aspect
@Component
@Slf4j
public class LogRecord {
    /*
      执行拦截
     */
    @Around("execution(* com.jhc.chathub.controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint point) throws Throwable {
        // 1.开启一个计时器
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // 2.输出请求日志
        // 2.1获取请求路径
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 2.2生成请求唯一 id
        String requestId = UUID.randomUUID().toString(true);
        String url = httpServletRequest.getRequestURI();
        // 2.3获取请求参数
        Object[] args = point.getArgs();
        String reqParam = "[" +  StringUtils.join(args, ", ") + "]";
        // 2.4打印日志
        log.info("request start，id: {}, path: {}, ip: {}, params: {}", requestId, url,
                httpServletRequest.getRemoteHost(), reqParam);

        // 3.执行原方法
        Object result = point.proceed();

        // 4.输出响应日志
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        log.info("request end, id: {}, cost: {} ms", requestId, totalTimeMillis);
        return result;
    }
}
