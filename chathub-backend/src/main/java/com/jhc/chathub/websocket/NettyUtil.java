package com.jhc.chathub.websocket;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

public class NettyUtil {
    public static AttributeKey<String> TOKEN = AttributeKey.valueOf("token"); // token
    public static AttributeKey<Long> UID = AttributeKey.valueOf("uid"); // uid
    public static AttributeKey<Long> ROOM_ID = AttributeKey.valueOf("roomId"); // 房间id

    public static <T> void setAttr(Channel channel, AttributeKey<T> attributeKey, T data) {
        channel.attr(attributeKey).set(data);
    }

    public static <T> T getAttr(Channel channel, AttributeKey<T> attributeKey) {
        return channel.attr(attributeKey).get();
    }
}
