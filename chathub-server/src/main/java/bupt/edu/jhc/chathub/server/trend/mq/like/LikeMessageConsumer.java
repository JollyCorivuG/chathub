package bupt.edu.jhc.chathub.server.trend.mq.like;

import bupt.edu.jhc.chathub.common.domain.constants.MqConstants;
import bupt.edu.jhc.chathub.server.trend.domain.entity.Like;
import bupt.edu.jhc.chathub.server.trend.mapper.LikeMapper;
import bupt.edu.jhc.chathub.server.trend.service.ITrendService;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: like 消息队列消费者
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/16
 */
@Component
@Slf4j
public class LikeMessageConsumer {
    @Resource
    private ITrendService trendService;

    @Resource
    private LikeMapper likeMapper;

    private Like queryLike(LikeMessage likeMsg) {
        QueryWrapper<Like> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", likeMsg.getUserId())
                .eq("talk_id", likeMsg.getTalkId());
        List<Like> likes = likeMapper.selectList(wrapper);
        return likes.isEmpty() ? null : likes.get(0);
    }

    private void doLike(LikeMessage likeMsg) {
        // 1.判断是否已经点赞
        Like like = queryLike(likeMsg);
        if (like != null) {
            // 1.1已经点赞的话就更新点赞时间
            like.setCreateTime(likeMsg.getCreateTime());
            likeMapper.updateById(like);
            return;
        }

        // 2.如果没点赞, 就需要更新说说的点赞数并且插入点赞记录
        like = new Like().setTalkId(likeMsg.getTalkId())
                .setUserId(likeMsg.getUserId())
                .setCreateTime(likeMsg.getCreateTime());
        likeMapper.insert(like);
        trendService.update().setSql("like_count = like_count + 1")
                .eq("id", likeMsg.getTalkId()).update();
    }

    private void doUnLike(LikeMessage likeMsg) {
        // 1.判断是否已经点赞
        Like like = queryLike(likeMsg);
        if (like == null) {
            return;
        }

        // 2.如果已经点赞, 就需要更新说说的点赞数并且删除点赞记录
        likeMapper.deleteById(like.getId());
        trendService.update().setSql("like_count = like_count - 1")
                .eq("id", likeMsg.getTalkId()).update();
    }


    @RabbitListener(queues = {MqConstants.LIKE_QUEUE}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("接收到消息：{}", message);
        try {
            // 1.将message转换为LikeMessage对象
            LikeMessage likeMsg = JSONUtil.toBean(message, LikeMessage.class);
            if (!LikeMessage.isValid(likeMsg)) {
                log.error("收到的消息格式不正确");
                channel.basicAck(deliveryTag, false);
                return;
            }

            // 2.判断是点赞还是取消点赞
            if (likeMsg.getIsLike()) {
                doLike(likeMsg);
            } else {
                doUnLike(likeMsg);
            }

            // 3.手动确认消息已经被消费
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
