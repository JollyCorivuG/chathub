package com.jhc.chathub.bizmq.feeds;

import com.jhc.chathub.common.constants.MqConstant;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/12
 */
@Slf4j
public class FeedsInitMain {
    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(MqConstant.FEEDS_EXCHANGE, "direct");
            channel.queueDeclare(MqConstant.FEEDS_QUEUE, true, false, false, null);
            channel.queueBind(MqConstant.FEEDS_QUEUE, MqConstant.FEEDS_EXCHANGE, MqConstant.FEEDS_ROUTING_KEY);
        } catch (Exception e) {
            log.error("初始化消息队列失败", e);
        }
    }
}
