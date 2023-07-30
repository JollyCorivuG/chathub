package com.jhc.chathub.service;

import com.jhc.chathub.common.resp.WSResponse;
import io.netty.channel.Channel;

public interface IWebSocketService {
    void authorize(Channel channel);
    void connect(Channel channel);
    void remove(Channel channel);
    void sendToAssignedRoom(Long roomId, WSResponse<?> wsResponse, Long skipUid);
    void showOnlineUser();
}
