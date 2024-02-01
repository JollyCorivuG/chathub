package bupt.edu.jhc.chathub.server.trend.mq.like;

import bupt.edu.jhc.chathub.common.domain.constants.MqConstants;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: like 消息队列初始化
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/16
 */
@Slf4j
public class LikeInitMain {
    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUsername("admin");
            factory.setPassword("admin123");
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(MqConstants.LIKE_EXCHANGE, "direct", true);
            channel.queueDeclare(MqConstants.LIKE_QUEUE, true, false, false, null);
            channel.queueBind(MqConstants.LIKE_QUEUE, MqConstants.LIKE_EXCHANGE, MqConstants.LIKE_ROUTING_KEY);
        } catch (Exception e) {
            log.error("初始化消息队列失败", e);
        }
    }
}
