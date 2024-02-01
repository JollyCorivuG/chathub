package bupt.edu.jhc.chathub.server.sse.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: sse 响应
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Data
@Accessors(chain = true)
@ApiModel("sse 响应")
public class SseResponse<T> {
    @ApiModelProperty("响应类型 0-刷新房间列表 1-强制下线")
    Integer type;
    @ApiModelProperty("响应数据")
    T data;
    public static <T> SseResponse<T> build(Integer type, T data) {
        SseResponse<T> response = new SseResponse<>();
        return response.setType(type).setData(data);
    }
}
