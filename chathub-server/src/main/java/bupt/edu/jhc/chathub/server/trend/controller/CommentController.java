package bupt.edu.jhc.chathub.server.trend.controller;

import bupt.edu.jhc.chathub.common.domain.vo.resp.Response;
import bupt.edu.jhc.chathub.common.utils.context.RequestHolder;
import bupt.edu.jhc.chathub.server.trend.domain.dto.comment.PostCommentDTO;
import bupt.edu.jhc.chathub.server.trend.domain.vo.CommentVO;
import bupt.edu.jhc.chathub.server.trend.service.ICommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description: 评论接口
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/22
 */
@RestController
@RequestMapping("/api/comments")
@Api(tags = "评论相关接口")
public class CommentController {
    @Resource
    private ICommentService commentService;

    @PostMapping("/post")
    @ApiOperation("发布评论")
    public Response<CommentVO> postComment(@RequestBody PostCommentDTO comment) {
        Long userId = RequestHolder.get().getUid();
        return Response.success(commentService.postComment(userId, comment));
    }
}
