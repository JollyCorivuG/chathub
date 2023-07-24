package com.jhc.chathub.utils;

import cn.hutool.core.util.StrUtil;

public class RegexUtils {
    public static boolean isPhoneInvalid(String phone){
        return mismatch(phone, RegexPatterns.PHONE_REGEX);
    }
    public static boolean isAccountInvalid(String account){
        return mismatch(account, RegexPatterns.ACCOUNT_REGEX);
    }

    // 校验是否不符合正则格式
    private static boolean mismatch(String str, String regex){
        if (StrUtil.isBlank(str)) {
            return true;
        }
        return !str.matches(regex);
    }
}
