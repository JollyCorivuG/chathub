package bupt.edu.jhc.chathub.common.utils.exception;

import bupt.edu.jhc.chathub.common.domain.enums.ErrorStatus;
import bupt.edu.jhc.chathub.common.domain.vo.resp.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @Description: 全局异常处理
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/18
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 系统异常处理
     * @param e
     * @return Response
     */
    @ExceptionHandler(RuntimeException.class)
    public Response<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("System exception!The reason is: {}", e.getMessage());
        return Response.fail(ErrorStatus.SYSTEM_ERROR);
    }

    /**
     * 业务异常处理
     * @param e
     * @return Response
     */
    @ExceptionHandler(BizException.class)
    public Response<?> bizExceptionHandler(BizException e) {
        log.error("Business exception!The reason is: {}", e.getMessage());
        return Response.fail(e.getCode(), e.getMessage());
    }
}
