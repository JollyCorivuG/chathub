package com.jhc.chathub.sse;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/20
 */
@Data
@Accessors(chain = true)
public class SseResponse<T> {
    Integer type;
    T data;
    public static <T> SseResponse<T> build(Integer type, T data) {
        SseResponse<T> response = new SseResponse<>();
        return response.setType(type).setData(data);
    }
}
