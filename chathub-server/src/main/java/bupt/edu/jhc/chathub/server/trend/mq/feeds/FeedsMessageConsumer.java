package bupt.edu.jhc.chathub.server.trend.mq.feeds;

import bupt.edu.jhc.chathub.common.domain.constants.MqConstants;
import bupt.edu.jhc.chathub.server.trend.domain.entity.Feeds;
import bupt.edu.jhc.chathub.server.trend.mapper.FeedsMapper;
import bupt.edu.jhc.chathub.server.user.service.IUserService;
import cn.hutool.json.JSONUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: feed 流消费者
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/12
 */
@Component
@Slf4j
public class FeedsMessageConsumer {
    @Resource
    private IUserService userService;

    @Resource
    private FeedsMapper feedsMapper;


    @RabbitListener(queues = {MqConstants.FEEDS_QUEUE}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("接收到消息：{}", message);
        try {
            // 1.将message转换为FeedsMessage对象
            FeedsMessage feedsMsg = JSONUtil.toBean(message, FeedsMessage.class);
            if (!FeedsMessage.isValid(feedsMsg)) {
                log.error("收到的消息格式不正确");
                channel.basicAck(deliveryTag, false);
                return;
            }

            // 2.根据authorId获取所有好友id
            List<Long> friendIds = userService.queryFriendIds(feedsMsg.getAuthorId());
            friendIds.forEach(id -> {
                // 2.1构建feeds对象, 插入到数据库
                Feeds feeds = new Feeds();
                feeds.setConnectUserId(id).setTalkId(feedsMsg.getTalkId());
                feedsMapper.insert(feeds);
            });

            // 3.手动确认消息已经被消费
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
