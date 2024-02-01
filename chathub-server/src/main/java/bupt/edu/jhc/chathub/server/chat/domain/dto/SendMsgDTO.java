package bupt.edu.jhc.chathub.server.chat.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: 发送消息请求
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Data
@Accessors(chain = true)
@ApiModel("发送消息请求")
public class SendMsgDTO {
    @ApiModelProperty("房间 id")
    private Long roomId;
    @ApiModelProperty("消息类型 0-文本消息 1-图片消息 2-文件消息")
    private Integer msgType;
    @ApiModelProperty("消息体")
    private Object body; // 消息的主体, 根据不同消息类型, 传不同的值
}
