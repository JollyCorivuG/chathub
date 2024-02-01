package bupt.edu.jhc.chathub.common.utils.regex;

import cn.hutool.core.util.StrUtil;

/**
 * @Description: 正则工具类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/18
 */
public class RegexUtils {
    public static boolean isPhoneValid(String phone){
        return match(phone, RegexPatterns.PHONE_REGEX);
    }
    public static boolean isAccountValid(String account){
        return match(account, RegexPatterns.ACCOUNT_REGEX);
    }

    // 校验是否不符合正则格式
    private static boolean match(String str, String regex){
        if (StrUtil.isBlank(str)) {
            return true;
        }
        return str.matches(regex);
    }
}
