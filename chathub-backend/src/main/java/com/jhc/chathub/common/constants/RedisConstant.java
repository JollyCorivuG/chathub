package com.jhc.chathub.common.constants;

public class RedisConstant {
    // token保存30min
    public static final String USER_TOKEN_KEY = "user:token:";
    public static final Integer USER_TOKEN_KEY_TTL = 30;

    // 手机验证码保存1min
    public static final String PHONE_CODE_KEY = "phone:code:";
    public static final Integer PHONE_CODE_KEY_TTL = 1;

    // 记录在线用户
    public static final String ID_TO_TOKEN = "token:id:";
    public static final String ONLINE_USER_KEY = "online:user";

    // 记录用户的好友
    public static final String USER_FRIEND_KEY = "user:friend:";

    // 缓存查询用户
    public static final String CACHE_QUERY_USER_KET = "cache:users:";
    public static final Integer CACHE_QUERY_USER_KEY_TTL = 5;
}
