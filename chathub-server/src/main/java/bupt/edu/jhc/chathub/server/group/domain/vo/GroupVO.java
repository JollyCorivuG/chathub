package bupt.edu.jhc.chathub.server.group.domain.vo;

import bupt.edu.jhc.chathub.server.user.domain.vo.UserVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description: 群组 vo
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("群组 vo")
public class GroupVO {
    @ApiModelProperty("群组 id")
    private Long id;
    @ApiModelProperty("群组号")
    private String number;
    @ApiModelProperty("群组名称")
    private String name;
    @ApiModelProperty("群组头像")
    private String avatar;
    @ApiModelProperty("群组人数")
    private Integer peopleNum;
    @ApiModelProperty("群主")
    private UserVO owner;
    @ApiModelProperty("群成员")
    private List<UserVO> members;
    @ApiModelProperty("群组聊天室 id")
    private Long roomId;
    @ApiModelProperty("群组创建时间")
    private LocalDateTime createTime;
}
