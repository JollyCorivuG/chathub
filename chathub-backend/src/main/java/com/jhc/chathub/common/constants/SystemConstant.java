package com.jhc.chathub.common.constants;

public class SystemConstant {
    // 响应码 0：成功 1：失败
    public static final Integer SUCCESS_REQUEST = 0;
    public static final Integer COMMON_ERROR = 1;

    // 默认用户前缀名
    public static final String DEFAULT_NICK_NAME_PREFIX = "user_";

    // 默认用户头像
    public static final String DEV_DEFAULT_USER_AVATAR_URL = "/src/assets/images/avatar/default_user_avatar.jpg";
    public static final String PROD_DEFAULT_USER_AVATAR_URL = "/usr/share/nginx/html//assets/images/avatar/default_user_avatar.jpg";

    // 查询用户的默认分页参数
    public static final Integer DEFAULT_PAGE_SIZE = 20;

    // 好友通知的一些常量
    public static final Integer NOTICE_TYPE_ADD_OTHER = 0; // 申请添加好友
    public static final Integer NOTICE_TYPE_OTHER_ADD_ME = 1; // 对方申请添加好友
    public static final Integer NOTICE_STATUS_WAIT = 0; // 待处理
    public static final Integer NOTICE_STATUS_PASS = 1; // 已通过
    public static final Integer NOTICE_STATUS_NOT_PASS = 2; // 未通过
    public static final Integer NOTICE_STATUS_REFUSE = 3; // 已拒绝
    public static final Integer NOTICE_STATUS_ACCEPT = 4; // 已接受
    public static final Integer NOTICE_STATUS_PENDING = 5; // 等待处理

    // 消息类型
    public static final Integer TEXT_MSG = 0;
    public static final Integer IMG_MSG = 1;
    public static final Integer FILE_MSG = 2;

    // WS响应消息类型
    public static final Integer WS_NORMAL_MSG = 0; // 普通消息
    public static final Integer WS_HEAD_SHAKE_SUCCESS_MSG = 1; // 握手成功消息
    public static final Integer WS_HEAD_SHAKE_FAIL_MSG = 2; // 握手失败消息

    // room类型
    public static final Integer ROOM_TYPE_SINGLE = 0; // 单聊
    public static final Integer ROOM_TYPE_GROUP = 1; // 群聊

    // 说说的额外内容的类型, 0表示图片, 1表示视频
    public static final Integer TALK_EXTRA_TYPE_IMG = 0;
    public static final Integer TALK_EXTRA_TYPE_VIDEO = 1;
}
