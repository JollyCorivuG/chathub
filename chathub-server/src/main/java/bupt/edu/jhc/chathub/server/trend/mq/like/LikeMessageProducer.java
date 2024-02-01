package bupt.edu.jhc.chathub.server.trend.mq.like;

import bupt.edu.jhc.chathub.common.domain.constants.MqConstants;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description: like 消息生产者
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/16
 */
@Component
public class LikeMessageProducer {
    @Resource
    private RabbitTemplate rabbitTemplate;

    // 发送消息
    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(MqConstants.LIKE_EXCHANGE, MqConstants.LIKE_ROUTING_KEY, message);
    }
}
