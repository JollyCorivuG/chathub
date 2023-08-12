package com.jhc.chathub.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.chathub.mapper.CommentMapper;
import com.jhc.chathub.pojo.dto.comment.PostCommentDTO;
import com.jhc.chathub.pojo.entity.Comment;
import com.jhc.chathub.pojo.vo.CommentVO;
import com.jhc.chathub.pojo.vo.UserVO;
import com.jhc.chathub.service.ICommentService;
import com.jhc.chathub.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/11
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {
    @Resource
    private IUserService userService;

    @Override
    public CommentVO convertCommentToVO(Comment comment) {
        // 1.先直接利用hutool工具进行拷贝
        CommentVO commentVO = BeanUtil.copyProperties(comment, CommentVO.class);

        // 2.查询发布该评论的用户信息
        UserVO sender = userService.convertUserToUserVO(comment.getSenderId(), userService.getById(comment.getSenderId()));
        commentVO.setSender(sender);

        // 3.查询被这条评论回复的用户信息
        if (comment.getReplyUserId() != 0) {
            UserVO replyUser = userService.convertUserToUserVO(comment.getReplyUserId(), userService.getById(comment.getReplyUserId()));
            commentVO.setReplyUser(replyUser);
        }

        // 4.返回
        return commentVO;
    }

    @Override
    public CommentVO postComment(Long userId, PostCommentDTO comment) {
        // 1.保存评论到数据库
        Comment saveComment = new Comment();
        saveComment.setSenderId(userId)
                .setTalkId(comment.getTalkId())
                .setContent(comment.getContent())
                .setReplyUserId(comment.getReplyUserId());
        save(saveComment);

        // 2.返回
        return convertCommentToVO(getById(saveComment.getId()));
    }
}
