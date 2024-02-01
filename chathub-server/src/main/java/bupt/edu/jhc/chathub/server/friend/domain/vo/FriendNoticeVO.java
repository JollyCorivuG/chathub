package bupt.edu.jhc.chathub.server.friend.domain.vo;

import bupt.edu.jhc.chathub.server.user.domain.vo.UserVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Description: 好友通知信息
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Data
@Accessors(chain = true)
@ApiModel("好友通知信息")
public class FriendNoticeVO {
    @ApiModelProperty("通知 id")
    private Long id;
    @ApiModelProperty("对方用户信息")
    private UserVO showUserInfo;
    @ApiModelProperty("描述信息")
    private String description;
    @ApiModelProperty("通知类型 0-申请添加好友 1-对方申请你添加好友")
    private Integer noticeType;
    @ApiModelProperty("状态信息")
    private Integer statusInfo;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
