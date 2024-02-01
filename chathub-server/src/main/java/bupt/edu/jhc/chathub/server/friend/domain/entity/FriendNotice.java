package bupt.edu.jhc.chathub.server.friend.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Description: 好友通知实体类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Data
@Accessors(chain = true)
@TableName("tb_friend_notice")
public class FriendNotice {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("connect_user_id")
    private Long connectUserId;
    @TableField("other_user_id")
    private Long otherUserId;
    @TableField("description")
    private String description;
    @TableField("notice_type")
    private Integer noticeType;
    @TableField("status_info")
    private Integer statusInfo;
    @TableField("create_time")
    private LocalDateTime createTime;
    @TableField("update_time")
    private LocalDateTime updateTime;
}
