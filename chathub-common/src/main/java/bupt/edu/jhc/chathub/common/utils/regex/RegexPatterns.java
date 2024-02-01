package bupt.edu.jhc.chathub.common.utils.regex;

/**
 * @Description: 正则表达式
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/18
 */
public abstract class RegexPatterns {
    /**
     * 手机号正则
     */
    public static final String PHONE_REGEX = "^1[3-9]\\d{9}$";
    /**
     * 账号正则，10位数字
     */
    public static final String ACCOUNT_REGEX = "^\\d{10}$";
}
