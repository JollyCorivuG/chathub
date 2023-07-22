package com.jhc.chathub.config;

import com.jhc.chathub.common.enums.ErrorStatus;
import com.jhc.chathub.common.resp.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Response<Void> handleRuntimeException(RuntimeException e) {
        log.error(e.toString(), e);
        return Response.fail(ErrorStatus.SERVER_ERROR);
    }
}
