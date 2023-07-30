package com.jhc.chathub.pojo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ShowMsgVO {
    private UserInfo fromUser;
    private MessageInfo message;

    @Data
    @Accessors(chain = true)
    public static class UserInfo {
        private Long id; // 只需要返回用户id即可
    }

    @Data
    @Accessors(chain = true)
    public static class MessageInfo {
        private Long id;
        private LocalDateTime sendTime;
        private Integer msgType;
        private Object body;
    }
}
