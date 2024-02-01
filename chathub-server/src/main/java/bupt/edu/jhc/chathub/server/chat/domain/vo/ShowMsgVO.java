package bupt.edu.jhc.chathub.server.chat.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Description: 消息 vo
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Data
@Accessors(chain = true)
@ApiModel
public class ShowMsgVO {
    @ApiModelProperty("发送者")
    private UserInfo fromUser;
    @ApiModelProperty("消息")
    private MessageInfo message;

    @Data
    @Accessors(chain = true)
    public static class UserInfo {
        private Long id; // 只需要返回用户id即可
    }

    @Data
    @Accessors(chain = true)
    public static class MessageInfo {
        @ApiModelProperty("消息id")
        private Long id;
        @ApiModelProperty("发送时间")
        private LocalDateTime sendTime;
        @ApiModelProperty("消息类型 0-文本 1-图片 2-文件")
        private Integer msgType;
        @ApiModelProperty("消息体")
        private Object body;
    }
}

