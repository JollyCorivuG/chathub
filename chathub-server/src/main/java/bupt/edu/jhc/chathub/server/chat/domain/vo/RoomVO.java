package bupt.edu.jhc.chathub.server.chat.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: 房间 vo
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Data
@Accessors(chain = true)
@ApiModel("房间")
public class RoomVO {
    @ApiModelProperty("房间id")
    private Long id;
    @ApiModelProperty("房间类型")
    private Integer roomType;
    @ApiModelProperty("相关联信息")
    private Object connectInfo; // 如果是私聊，这里是对方用户信息; 如果是群聊，这里是群聊信息
    @ApiModelProperty("最新消息")
    private ShowMsgVO latestMsg; // 最新消息
    @ApiModelProperty("未读消息数")
    private Integer unreadCount; // 未读消息数
}

