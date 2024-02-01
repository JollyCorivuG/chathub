package bupt.edu.jhc.chathub.server.chat.domain.entity;

import bupt.edu.jhc.chathub.server.chat.domain.dto.MessageExtra;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Description: 消息实体
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Data
@Accessors(chain = true)
@TableName(value = "tb_message", autoResultMap = true)
public class Message {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "room_id")
    private Long roomId; // 会话id
    @TableField(value = "from_user_id")
    private Long fromUserId;
    @TableField(value = "content")
    private String content;
    @TableField(value = "msg_type")
    private Integer msgType; // 消息类型
    @TableField(value = "extra", typeHandler = JacksonTypeHandler.class)
    private MessageExtra extra; // 消息的扩展字段，包括图片、文件、表情包
    @TableField(value = "create_time")
    private LocalDateTime createTime;
    @TableField(value = "update_time")
    private LocalDateTime updateTime;
}
