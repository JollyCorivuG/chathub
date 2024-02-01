package bupt.edu.jhc.chathub.server.trend.service;

import bupt.edu.jhc.chathub.server.trend.domain.dto.comment.PostCommentDTO;
import bupt.edu.jhc.chathub.server.trend.domain.entity.Comment;
import bupt.edu.jhc.chathub.server.trend.domain.vo.CommentVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 评论服务接口
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/22
 */
public interface ICommentService extends IService<Comment> {
    CommentVO convertCommentToVO(Comment comment);

    CommentVO postComment(Long userId, PostCommentDTO comment);
}
