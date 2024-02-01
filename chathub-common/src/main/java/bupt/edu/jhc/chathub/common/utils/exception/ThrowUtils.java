package bupt.edu.jhc.chathub.common.utils.exception;

import bupt.edu.jhc.chathub.common.domain.enums.ErrorStatus;

/**
 * @Description: 抛出异常工具类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/18
 */
public class ThrowUtils {
    /**
     * 条件成立则抛异常
     * @param condition
     * @param bizException
     */
    public static void throwIf(boolean condition, BizException bizException) {
        if (condition) {
            throw bizException;
        }
    }
    public static void throwIf(boolean condition, ErrorStatus errorStatus) {
        throwIf(condition, new BizException(errorStatus));
    }
    public static void throwIf(boolean condition, ErrorStatus errorStatus, String message) {
        throwIf(condition, new BizException(errorStatus, message));
    }
}
