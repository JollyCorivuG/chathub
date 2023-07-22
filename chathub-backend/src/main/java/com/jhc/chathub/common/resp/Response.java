package com.jhc.chathub.common.resp;

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
        return response.setStatusCode(0).setStatusMsg("成功").setData(data);
    }
    public static <T> Response<T> fail(ErrorStatus errorStatus) {
        Response<T> response = new Response<>();
        return response.setStatusCode(errorStatus.getCode()).setStatusMsg(errorStatus.getMessage());
    }
}
