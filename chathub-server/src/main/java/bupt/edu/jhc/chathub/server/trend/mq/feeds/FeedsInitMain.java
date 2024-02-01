package bupt.edu.jhc.chathub.server.trend.mq.feeds;

import bupt.edu.jhc.chathub.common.domain.constants.MqConstants;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: feed 流消息队列初始化
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/12
 */
@Slf4j
public class FeedsInitMain {

    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUsername("admin");
            factory.setPassword("admin123");
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(MqConstants.FEEDS_EXCHANGE, "direct", true);
            channel.queueDeclare(MqConstants.FEEDS_QUEUE, true, false, false, null);
            channel.queueBind(MqConstants.FEEDS_QUEUE, MqConstants.FEEDS_EXCHANGE, MqConstants.FEEDS_ROUTING_KEY);
        } catch (Exception e) {
            log.error("初始化消息队列失败", e);
        }
    }
}

