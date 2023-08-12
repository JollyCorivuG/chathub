package com.jhc.chathub.bizmq.feeds;

import com.jhc.chathub.common.constants.MqConstant;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/12
 */
@Component
public class FeedsMessageProducer {
    @Resource
    private RabbitTemplate rabbitTemplate;

    // 发送消息
    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(MqConstant.FEEDS_EXCHANGE, MqConstant.FEEDS_ROUTING_KEY, message);
    }
}
