package bupt.edu.jhc.chathub.common.domain.vo.resp;

import bupt.edu.jhc.chathub.common.domain.constants.SystemConstants;
import bupt.edu.jhc.chathub.common.domain.enums.ErrorStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: 通用响应类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/18
 */
@Data
@Accessors(chain = true)
@ApiModel("基础响应体")
public class Response<T> {
    @ApiModelProperty("状态码 0-成功 其他值-失败")
    private Integer statusCode;
    @ApiModelProperty("状态信息")
    private String statusMsg;
    @ApiModelProperty("响应数据")
    private T data;
    public static <T> Response<T> success(T data) {
        Response<T> response = new Response<>();
        return response
                .setStatusCode(SystemConstants.SUCCESS_REQUEST)
                .setStatusMsg("成功")
                .setData(data);
    }
    public static <T> Response<T> fail(ErrorStatus errorStatus) {
        Response<T> response = new Response<>();
        return response
                .setStatusCode(errorStatus.getCode())
                .setStatusMsg(errorStatus.getMessage());
    }
    public static <T> Response<T> fail(Integer code, String message) {
        Response<T> response = new Response<>();
        return response
                .setStatusCode(code)
                .setStatusMsg(message);
    }
}
