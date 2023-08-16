package com.jhc.chathub.bizmq.like;

import com.jhc.chathub.common.constants.MqConstant;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/16
 */
@Component
public class LikeMessageProducer {
    @Resource
    private RabbitTemplate rabbitTemplate;

    // 发送消息
    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(MqConstant.LIKE_EXCHANGE, MqConstant.LIKE_ROUTING_KEY, message);
    }
}
