package com.jhc.chathub.pojo.dto.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageExtra {
    private ImgMsgDTO imgMsg; // 图片消息
    private FileMsgDTO fileMsg; // 文件消息
}
