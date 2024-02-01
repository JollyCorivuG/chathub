package bupt.edu.jhc.chathub.server.trend.service.impl;

import bupt.edu.jhc.chathub.server.trend.domain.dto.comment.PostCommentDTO;
import bupt.edu.jhc.chathub.server.trend.domain.entity.Comment;
import bupt.edu.jhc.chathub.server.trend.domain.vo.CommentVO;
import bupt.edu.jhc.chathub.server.trend.mapper.CommentMapper;
import bupt.edu.jhc.chathub.server.trend.service.ICommentService;
import bupt.edu.jhc.chathub.server.user.domain.vo.UserVO;
import bupt.edu.jhc.chathub.server.user.service.IUserService;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: 评论服务实现类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/22
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

