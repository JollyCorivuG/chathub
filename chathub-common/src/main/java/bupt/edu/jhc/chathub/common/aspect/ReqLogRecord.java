package bupt.edu.jhc.chathub.common.aspect;

import bupt.edu.jhc.chathub.common.domain.constants.MDCKeys;
import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 接口请求日志记录
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/18
 */
@Aspect
@Component
@Slf4j
public class ReqLogRecord {
    @Around("execution(* bupt.edu.jhc.chathub.server.*.controller..*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint point) throws Throwable {
        // 1.开启一个计时器
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // 2.输出请求日志
        // 2.1获取请求路径
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 2.2生成链路唯一 id
        String tId = UUID.randomUUID().toString();
        MDC.put(MDCKeys.TID, tId);
        String url = httpServletRequest.getRequestURI();
        // 2.3获取请求参数
        Object[] args = point.getArgs();
        String reqParam = "[" +  StringUtils.join(args, ", ") + "]";
        // 2.4打印日志
        log.info("request start, path: {}, ip: {}, params: {}", url,
                httpServletRequest.getRemoteHost(), reqParam);

        // 3.执行原方法
        Object result = point.proceed();
        MDC.remove(MDCKeys.TID);

        // 4.输出响应日志
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        log.info("request end, cost: {} ms", totalTimeMillis);
        return result;
    }
}
