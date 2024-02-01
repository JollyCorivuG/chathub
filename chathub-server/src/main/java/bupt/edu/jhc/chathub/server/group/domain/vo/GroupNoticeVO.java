package bupt.edu.jhc.chathub.server.group.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Description: 群组通知 vo
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("群组通知 vo")
public class GroupNoticeVO {
    @ApiModelProperty("通知 id")
    private Long id;
    @ApiModelProperty("群组信息")
    private GroupVO showGroupInfo;
    @ApiModelProperty("通知描述")
    private String description;
    @ApiModelProperty("通知状态")
    private Integer statusInfo;
    @ApiModelProperty("通知创建时间")
    private LocalDateTime createTime;
}
