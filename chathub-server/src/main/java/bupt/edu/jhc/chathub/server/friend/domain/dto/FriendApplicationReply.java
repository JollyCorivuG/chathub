package bupt.edu.jhc.chathub.server.friend.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 好友申请回复
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Data
@ApiModel("好友申请回复")
public class FriendApplicationReply {
    @ApiModelProperty("是否接受")
    private Boolean isAccept;
    @ApiModelProperty("通知 id")
    private Long noticeId;
    @ApiModelProperty("用户 id")
    private Long userId;
}
