package com.jhc.chathub.common.resp;

import com.jhc.chathub.common.constants.SystemConstant;
import com.jhc.chathub.common.enums.ErrorStatus;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Response<T> {
    private Integer statusCode;
    private String statusMsg;
    private T data;
    public static <T> Response<T> success(T data) {
        Response<T> response = new Response<>();
        return response.setStatusCode(SystemConstant.SUCCESS_REQUEST).setStatusMsg("成功").setData(data);
    }
    public static <T> Response<T> fail(ErrorStatus errorStatus) {
        Response<T> response = new Response<>();
        return response.setStatusCode(errorStatus.getCode()).setStatusMsg(errorStatus.getMessage());
    }
    public static <T> Response<T> fail(Integer code, String message) {
        Response<T> response = new Response<>();
        return response.setStatusCode(code).setStatusMsg(message);
    }
}
