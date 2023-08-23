package com.jhc.chathub.bizmq.like;

import com.jhc.chathub.common.constants.MqConstant;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/16
 */
@Slf4j
public class LikeInitMain {
    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(MqConstant.LIKE_EXCHANGE, "direct", true);
            channel.queueDeclare(MqConstant.LIKE_QUEUE, true, false, false, null);
            channel.queueBind(MqConstant.LIKE_QUEUE, MqConstant.LIKE_EXCHANGE, MqConstant.LIKE_ROUTING_KEY);
        } catch (Exception e) {
            log.error("初始化消息队列失败", e);
        }
    }
}
