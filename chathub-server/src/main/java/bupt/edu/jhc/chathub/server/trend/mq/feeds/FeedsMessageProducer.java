package bupt.edu.jhc.chathub.server.trend.mq.feeds;

import bupt.edu.jhc.chathub.common.domain.constants.MqConstants;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description: feed 流生产者
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/12
 */
@Component
public class FeedsMessageProducer {
    @Resource
    private RabbitTemplate rabbitTemplate;

    // 发送消息
    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(MqConstants.FEEDS_EXCHANGE, MqConstants.FEEDS_ROUTING_KEY, message);
    }
}
