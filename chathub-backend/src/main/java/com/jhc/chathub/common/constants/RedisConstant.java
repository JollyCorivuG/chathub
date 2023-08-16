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
    public static final Integer CACHE_QUERY_USER_KEY_TTL = 2;

    // 记录用户对于一个房间最新的已读消息id, key为用户id:房间id
    public static final String USER_READ_LATEST_MESSAGE = "user:read:latest:message:";

    // 记录每个房间最新的消息id, key为房间id
    public static final String ROOM_LATEST_MESSAGE = "room:latest:message:";

    // 记录用户删除一个会话时, 当前会话的最新消息id, key为用户id:会话id
    public static final String USER_DELETE_LATEST_MESSAGE = "user:delete:latest:message:";

    // 记录某个说说最新点赞的用户id, key为说说id
    public static final String TALK_LATEST_LIKE = "talk:latest:like:";

}
