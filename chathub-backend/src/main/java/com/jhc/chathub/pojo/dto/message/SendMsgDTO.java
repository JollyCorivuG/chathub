package com.jhc.chathub.pojo.dto.message;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SendMsgDTO {
    private Long roomId;
    private Integer msgType;
    private Object body; // 消息的主体, 根据不同消息类型, 传不同的值
}
