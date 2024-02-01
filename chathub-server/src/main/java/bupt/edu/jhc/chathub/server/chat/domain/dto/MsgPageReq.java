package bupt.edu.jhc.chathub.server.chat.domain.dto;

import bupt.edu.jhc.chathub.common.domain.vo.request.CursorPageBaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description: 消息翻页请求
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("消息翻页请求")
public class MsgPageReq extends CursorPageBaseReq {
    @ApiModelProperty("房间 id")
    private Long roomId;
}
