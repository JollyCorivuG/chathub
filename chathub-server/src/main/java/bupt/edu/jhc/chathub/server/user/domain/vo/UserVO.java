package bupt.edu.jhc.chathub.server.user.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Description: 用户 vo
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Data
@Accessors(chain = true)
@ApiModel("用户信息")
public class UserVO {
    @ApiModelProperty("用户 id")
    private Long id;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("账号")
    private String account;
    @ApiModelProperty("昵称")
    private String nickName;
    @ApiModelProperty("头像")
    private String avatarUrl;
    @ApiModelProperty("等级")
    private Integer level;
    @ApiModelProperty("好友数")
    private Integer friendCount;
    @ApiModelProperty("群组数")
    private Integer groupCount;
    @ApiModelProperty("是否在线")
    private Boolean isOnline;
    @ApiModelProperty("是否为好友")
    private Boolean isFriend;
    @ApiModelProperty("成为好友时间")
    private LocalDateTime becomeFriendTime;
    @ApiModelProperty("属于的房间 id")
    private Long roomId;
}
