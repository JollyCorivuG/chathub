package bupt.edu.jhc.chathub.server.trend.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Description: 评论实体类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/22
 */
@Data
@Accessors(chain = true)
@TableName("tb_comment")
public class Comment {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("sender_id")
    private Long senderId;
    @TableField("talk_id")
    private Long talkId;
    @TableField("content")
    private String content;
    @TableField("reply_user_id")
    private Long replyUserId;
    @TableField("create_time")
    private LocalDateTime createTime;
    @TableField("update_time")
    private LocalDateTime updateTime;
}
