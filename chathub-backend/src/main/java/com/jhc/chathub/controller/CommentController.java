package com.jhc.chathub.controller;

import com.jhc.chathub.common.resp.Response;
import com.jhc.chathub.pojo.dto.comment.PostCommentDTO;
import com.jhc.chathub.pojo.vo.CommentVO;
import com.jhc.chathub.service.ICommentService;
import com.jhc.chathub.utils.UserHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/11
 */
@RestController
@RequestMapping("/comments")
@Tag(name = "评论相关接口")
public class CommentController {
    @Resource
    private ICommentService commentService;

    @PostMapping("/post")
    @Operation(summary = "发布评论")
    public Response<CommentVO> postComment(@RequestBody PostCommentDTO comment) {
        Long userId = UserHolder.getUser().getId();
        return Response.success(commentService.postComment(userId, comment));
    }
}
