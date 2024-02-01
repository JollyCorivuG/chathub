package bupt.edu.jhc.chathub.server.sse.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: sse 响应类型
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
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
