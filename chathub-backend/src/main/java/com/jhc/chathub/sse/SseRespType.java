package com.jhc.chathub.sse;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/20
 */
@Getter
@AllArgsConstructor
public enum SseRespType {
    FRESH_ROOM_LIST(0, "刷新房间列表"),
    FORCE_LOGOUT(1, "登录多个账号时强制下线"),
    ;

    private final Integer code;
    private final String description;
}
