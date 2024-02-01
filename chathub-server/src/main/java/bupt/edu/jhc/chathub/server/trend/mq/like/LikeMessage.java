package bupt.edu.jhc.chathub.server.trend.mq.like;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Description: like 消息
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/16
 */
@Data
@Accessors(chain = true)
public class LikeMessage {
    private Long userId;
    private Long talkId;
    private Boolean isLike;
    private LocalDateTime createTime;

    public static boolean isValid(LikeMessage likeMessage) {
        return likeMessage != null && likeMessage.getUserId() != null && likeMessage.getTalkId() != null && likeMessage.getIsLike() != null;
    }
}
