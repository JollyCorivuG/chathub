package com.jhc.chathub.common.constants;

public class RedisConstant {
    // token保存30min
    public static final String USER_TOKEN_KEY = "user:token:";
    public static final Integer USER_TOKEN_KEY_TTL = 30;

    // 手机验证码保存1min
    public static final String PHONE_CODE_KEY = "phone:code:";
    public static final Integer PHONE_CODE_KEY_TTL = 1;
}
