package bupt.edu.jhc.chathub.server.chat.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description: 消息类型枚举
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@AllArgsConstructor
@Getter
public enum MsgTypeEnum {
    TEXT(0, "文本消息"),
    IMG(1, "图片消息"),
    FILE(2, "文件消息"),
    ;

    private final Integer type;
    private final String description;

    private static final Map<Integer, MsgTypeEnum> cache;

    static {
        cache = Arrays.stream(MsgTypeEnum.values()).collect(Collectors.toMap(MsgTypeEnum::getType, Function.identity()));
    }

    public static MsgTypeEnum of(Integer type) {
        return cache.get(type);
    }
}
