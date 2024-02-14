package bupt.edu.jhc.chathub.server.chat.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Description: 用户与房间的关系
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/2/11
 */
@Data
@Accessors(chain = true)
@TableName(value = "tb_user_room")
public class UserRoom {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "user_id")
    private Long userId;
    @TableField(value = "room_id")
    private Long roomId;
    @TableField(value = "latest_del_msg_id")
    private Long latestDelMsgId;
    @TableField(value = "latest_read_msg_id")
    private Long latestReadMsgId;

    @TableField(value = "create_time")
    @JsonIgnore
    private LocalDateTime createTime;
    @TableField(value = "update_time")
    @JsonIgnore
    private LocalDateTime updateTime;
}
