package com.jhc.chathub.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum WSReqEnum {
    HEARTBEAT(0, "心跳包"),
    AUTHORIZE(1, "登录认证"),
    ;

    private final Integer type;
    private final String desc;

    private static final Map<Integer, WSReqEnum> cache;

    static {
        cache = Arrays.stream(WSReqEnum.values()).collect(Collectors.toMap(WSReqEnum::getType, Function.identity()));
    }


    public static WSReqEnum of(Integer type) {
        return cache.get(type);
    }
}
