package bupt.edu.jhc.chathub.server.friend.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 好友申请
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Data
@ApiModel("好友申请")
public class FriendApplication {
    @ApiModelProperty("对方 id")
    private Long toUserId;
    @ApiModelProperty("描述信息")
    private String description;
}
