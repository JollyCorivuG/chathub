package com.jhc.chathub.common.exception;

import com.jhc.chathub.common.enums.ErrorStatus;
import com.jhc.chathub.common.resp.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Description: 全局异常处理器
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/23
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public Response<Void> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return Response.fail(ErrorStatus.SYSTEM_ERROR);
    }

    @ExceptionHandler(BizException.class)
    public Response<Void> bizExceptionHandler(BizException e) {
        log.error("BizException", e);
        return Response.fail(e.getCode(), e.getMessage());
    }
}
