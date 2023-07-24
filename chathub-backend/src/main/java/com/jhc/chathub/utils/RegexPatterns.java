package com.jhc.chathub.utils;

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
