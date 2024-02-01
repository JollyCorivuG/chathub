package bupt.edu.jhc.chathub.common.utils.exception;

import bupt.edu.jhc.chathub.common.domain.enums.ErrorStatus;
import lombok.Getter;

/**
 * @Description: 业务异常类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/18
 */
@Getter
public class BizException extends RuntimeException {
    private final Integer code;

    public BizException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.code = errorStatus.getCode();
    }

    public BizException(ErrorStatus errorStatus, String message) {
        super(message);
        this.code = errorStatus.getCode();
    }
}
