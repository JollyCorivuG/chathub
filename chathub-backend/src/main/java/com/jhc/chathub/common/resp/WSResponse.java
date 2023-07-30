package com.jhc.chathub.common.resp;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WSResponse<T> {
    Integer type;
    T data;
    public static <T> WSResponse<T> build(Integer type, T data) {
        WSResponse<T> response = new WSResponse<>();
        return response.setType(type).setData(data);
    }
}
