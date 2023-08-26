package com.jhc.chathub.pojo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RoomVO {
    private Long id;
    private Integer roomType;
    private Object connectInfo; // 如果是私聊，这里是对方用户信息; 如果是群聊，这里是群聊信息
    private ShowMsgVO latestMsg; // 最新消息
    private Integer unreadCount; // 未读消息数
}
