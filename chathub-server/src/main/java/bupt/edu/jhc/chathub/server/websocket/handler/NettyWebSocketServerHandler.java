package bupt.edu.jhc.chathub.server.websocket.handler;

import bupt.edu.jhc.chathub.server.websocket.domain.enums.WSReqEnum;
import bupt.edu.jhc.chathub.server.websocket.domain.req.WSReqDTO;
import bupt.edu.jhc.chathub.server.websocket.service.IWebSocketService;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: ws 服务处理器
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Slf4j
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private final IWebSocketService webSocketService = SpringUtil.getBean(IWebSocketService.class);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        log.info("客户端尝试连接");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        webSocketService.remove(ctx.channel());
        webSocketService.showOnlineUser();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.warn("触发 channelInactive 掉线![{}]", ctx.channel().id());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent)evt;
            // 如果处于读空闲状态，则关闭连接
            if (idleStateEvent.state() == IdleStateEvent.READER_IDLE_STATE_EVENT.state()) {
                webSocketService.remove(ctx.channel());
                ctx.channel().close();
                webSocketService.showOnlineUser();
            }
        } else if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            // 握手成功后, 需要进行认证
            webSocketService.authorize(ctx.channel());
            webSocketService.showOnlineUser();
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        WSReqDTO wsReq = JSONUtil.toBean(msg.text(), WSReqDTO.class);
        WSReqEnum wsReqEnum = WSReqEnum.of(wsReq.getType());
        switch (wsReqEnum) {
            case HEARTBEAT: {
                break;
            }
            case AUTHORIZE: {
                log.info("收到认证包");
                break;
            }
            default: {
                log.info("收到未知类型的包");
                break;
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("NettyWebSocketServerHandler Exception!", cause);
        Channel channel = ctx.channel();
        if (channel.isActive()) ctx.close();
    }
}
