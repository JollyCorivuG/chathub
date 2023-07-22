package com.jhc.chathub.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorStatus {
    SERVER_ERROR(500, "服务器错误"),
    BAD_REQUEST(400, "请求参数错误"),
    ;

    private final Integer code;
    private final String message;
}
