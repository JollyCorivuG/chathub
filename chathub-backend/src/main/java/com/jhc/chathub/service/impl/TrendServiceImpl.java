package com.jhc.chathub.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.chathub.bizmq.feeds.FeedsMessage;
import com.jhc.chathub.bizmq.feeds.FeedsMessageProducer;
import com.jhc.chathub.bizmq.like.LikeMessage;
import com.jhc.chathub.bizmq.like.LikeMessageProducer;
import com.jhc.chathub.common.constants.RedisConstant;
import com.jhc.chathub.common.request.CursorPageBaseReq;
import com.jhc.chathub.common.resp.CursorPageBaseResp;
import com.jhc.chathub.mapper.TalkMapper;
import com.jhc.chathub.pojo.dto.talk.CreateTalkDTO;
import com.jhc.chathub.pojo.entity.Feeds;
import com.jhc.chathub.pojo.entity.Talk;
import com.jhc.chathub.pojo.vo.CommentVO;
import com.jhc.chathub.pojo.vo.TalkVO;
import com.jhc.chathub.pojo.vo.UserVO;
import com.jhc.chathub.service.ICommentService;
import com.jhc.chathub.service.ITrendService;
import com.jhc.chathub.service.IUserService;
import com.jhc.chathub.utils.CursorUtils;
import com.jhc.chathub.utils.UserHolder;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Resource
    private LikeMessageProducer likeMessageProducer;

    @Resource
    private FeedsServiceImpl feedsService;

    @Override
    public TalkVO convertTalkToVO(Talk talk) {
        // 1.先直接利用hutool工具类将talk转换为VO
        TalkVO talkVO = BeanUtil.copyProperties(talk, TalkVO.class);

        // 2.查询作者信息
        UserVO author = userService.convertUserToUserVO(talk.getAuthorId(), userService.getById(talk.getAuthorId()));
        talkVO.setAuthor(author);

        // 3.查询是否点赞
        talkVO.setIsLike(stringRedisTemplate.opsForZSet().score(RedisConstant.TALK_LATEST_LIKE + talk.getId(), UserHolder.getUser().getId().toString()) != null);

        // 4.查询最新点赞的五个用户
        List<Long> userIds = Objects.requireNonNull(stringRedisTemplate.opsForZSet()
                .reverseRange(RedisConstant.TALK_LATEST_LIKE + talk.getId(), 0, 4))
                .stream().map(Long::parseLong).toList();
        List<UserVO> latestLikeUsers = userIds.stream().map(id -> userService.convertUserToUserVO(id, userService.getById(id))).toList();
        talkVO.setLatestLikeUsers(latestLikeUsers);

        // 5.查询评论
        List<CommentVO> comments = commentService.query().eq("talk_id", talk.getId()).orderByAsc("id").list()
                .stream().map(commentService::convertCommentToVO).toList();
        talkVO.setComments(comments);

        // 6.返回
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

        // 2.保存自己的feeds到数据库
        Feeds feeds = new Feeds();
        feeds.setConnectUserId(userId)
                .setTalkId(saveTalk.getId());
        feedsService.save(feeds);

        // 3.采用推模式将说说发布这一事件推送到消息队列, 由消息队列异步处理, 将feeds流推送给好友
        FeedsMessage feedsMessage = new FeedsMessage();
        feedsMessage.setAuthorId(userId).setTalkId(saveTalk.getId());
        feedsMessageProducer.sendMessage(JSONUtil.toJsonStr(feedsMessage));

        // 3.将说说转换为VO返回
        return convertTalkToVO(getById(saveTalk.getId()));
    }

    @Override
    public CursorPageBaseResp<TalkVO> getTalkPage(Long userId, CursorPageBaseReq req) {
        // 1.先根据userId查询出来Feeds
        CursorPageBaseResp<Feeds> feedsPage = CursorUtils.getCursorPageByMysql(feedsService, req, wrapper -> wrapper.eq(Feeds::getConnectUserId, userId), Feeds::getTalkId);
        if (feedsPage.isEmpty()) {
            return CursorPageBaseResp.empty();
        }

        // 2.再根据Feeds中的talkId查询出来talk并按照Feeds中的顺序排序
        return CursorPageBaseResp.change(feedsPage,
                query().in("id", feedsPage.getList().stream().map(Feeds::getTalkId).toList()).orderByDesc("id").list().stream().map(this::convertTalkToVO).toList());
    }

    @Override
    public void likeTalk(Long userId, Long talkId) {
        // 1.构建点赞信息
        LikeMessage likeMsg = new LikeMessage();
        likeMsg.setUserId(userId).setTalkId(talkId).setCreateTime(LocalDateTime.now()).setIsLike(true);

        // 2.插入到redis中, 按照时间排序
        stringRedisTemplate.opsForZSet().add(RedisConstant.TALK_LATEST_LIKE + talkId, userId.toString(), likeMsg.getCreateTime().toEpochSecond(java.time.ZoneOffset.UTC));

        // 3.发送消息到消息队列
        likeMessageProducer.sendMessage(JSONUtil.toJsonStr(likeMsg));
    }

    @Override
    public void cancelLikeTalk(Long userId, Long talkId) {
        // 1.构建点赞信息
        LikeMessage likeMsg = new LikeMessage();
        likeMsg.setUserId(userId).setTalkId(talkId).setCreateTime(LocalDateTime.now()).setIsLike(false);

        // 2.从redis中删除
        stringRedisTemplate.opsForZSet().remove(RedisConstant.TALK_LATEST_LIKE + talkId, userId.toString());

        // 3.发送消息到消息队列
        likeMessageProducer.sendMessage(JSONUtil.toJsonStr(likeMsg));
    }
}
