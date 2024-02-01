package bupt.edu.jhc.chathub.server.websocket.service;

import bupt.edu.jhc.chathub.server.websocket.domain.resp.WSResponse;
import io.netty.channel.Channel;

/**
 * @Description: ws 服务接口
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
public interface IWebSocketService {
    void authorize(Channel channel);
    void connect(Channel channel);
    void remove(Channel channel);
    void sendToAssignedRoom(Long roomId, WSResponse<?> wsResponse, Long skipUid);
    void showOnlineUser();

    boolean isExist(Long userId);
}
