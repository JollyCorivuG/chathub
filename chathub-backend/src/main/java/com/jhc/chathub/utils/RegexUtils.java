package com.jhc.chathub.utils;

import cn.hutool.core.util.StrUtil;

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
