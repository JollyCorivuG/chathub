package com.jhc.chathub.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.jhc.chathub.pojo.dto.message.MessageExtra;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName(value = "tb_message", autoResultMap = true)
public class Message {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long roomId; // 会话id
    private Long fromUserId;
    private String content;
    private Integer msgType; // 消息类型
    @TableField(value = "extra", typeHandler = JacksonTypeHandler.class)
    private MessageExtra extra; // 消息的扩展字段，包括图片、文件、表情包
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
