package com.jhc.chathub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.chathub.pojo.dto.comment.PostCommentDTO;
import com.jhc.chathub.pojo.entity.Comment;
import com.jhc.chathub.pojo.vo.CommentVO;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/11
 */
public interface ICommentService extends IService<Comment> {
    CommentVO convertCommentToVO(Comment comment);

    CommentVO postComment(Long userId, PostCommentDTO comment);
}
