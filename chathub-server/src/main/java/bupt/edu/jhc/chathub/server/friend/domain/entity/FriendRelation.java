package bupt.edu.jhc.chathub.server.friend.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Description: 好友关系实体类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Data
@Accessors(chain = true)
@TableName("tb_friend_relation")
public class FriendRelation {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("user_id1")
    private Long userId1;
    @TableField("user_id2")
    private Long userId2;
    @TableField("room_id")
    private Long roomId; // 两个人所属的会话id
    @TableField("create_time")
    private LocalDateTime createTime;
    @TableField("update_time")
    private LocalDateTime updateTime;
}
