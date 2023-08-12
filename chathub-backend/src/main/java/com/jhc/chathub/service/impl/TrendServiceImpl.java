package com.jhc.chathub.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.chathub.bizmq.feeds.FeedsMessage;
import com.jhc.chathub.bizmq.feeds.FeedsMessageProducer;
import com.jhc.chathub.common.constants.RedisConstant;
import com.jhc.chathub.mapper.TalkMapper;
import com.jhc.chathub.pojo.dto.talk.CreateTalkDTO;
import com.jhc.chathub.pojo.entity.Talk;
import com.jhc.chathub.pojo.vo.CommentVO;
import com.jhc.chathub.pojo.vo.TalkVO;
import com.jhc.chathub.pojo.vo.UserVO;
import com.jhc.chathub.service.ICommentService;
import com.jhc.chathub.service.ITrendService;
import com.jhc.chathub.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/11
 */
@Service
public class TrendServiceImpl extends ServiceImpl<TalkMapper, Talk> implements ITrendService {
    @Resource
    private IUserService userService;

    @Resource
    private ICommentService commentService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private FeedsMessageProducer feedsMessageProducer;

    @Override
    public TalkVO convertTalkToVO(Talk talk) {
        // 1.先直接利用hutool工具类将talk转换为VO
        TalkVO talkVO = BeanUtil.copyProperties(talk, TalkVO.class);

        // 2.查询作者信息
        UserVO author = userService.convertUserToUserVO(talk.getAuthorId(), userService.getById(talk.getAuthorId()));
        talkVO.setAuthor(author);

        // 3.查询最新点赞的五个用户
        List<Long> userIds = Objects.requireNonNull(stringRedisTemplate.opsForZSet()
                .reverseRange(RedisConstant.TALK_LATEST_LIKE + talk.getId(), 0, 4))
                .stream().map(Long::parseLong).toList();
        List<UserVO> latestLikeUsers = userIds.stream().map(id -> userService.convertUserToUserVO(id, userService.getById(id))).toList();
        talkVO.setLatestLikeUsers(latestLikeUsers);

        // 4.查询评论
        List<CommentVO> comments = commentService.query().eq("talk_id", talk.getId()).list()
                .stream().map(commentService::convertCommentToVO).toList();
        talkVO.setComments(comments);

        // 5.返回
        return talkVO;
    }

    @Override
    public TalkVO createTalk(Long userId, CreateTalkDTO talk) {
        // 1.保存说说到数据
        Talk saveTalk = new Talk();
        saveTalk.setAuthorId(userId)
                .setContent(talk.getContent())
                .setExtra(talk.getExtra());
        save(saveTalk);

        // 2.采用推模式将说说发布这一事件推送到消息队列, 由消息队列异步处理, 将feeds流推送给好友
        FeedsMessage feedsMessage = new FeedsMessage();
        feedsMessage.setAuthorId(userId).setTalkId(saveTalk.getId());
        feedsMessageProducer.sendMessage(JSONUtil.toJsonStr(feedsMessage));

        // 3.将说说转换为VO返回
        return convertTalkToVO(getById(saveTalk.getId()));
    }
}
