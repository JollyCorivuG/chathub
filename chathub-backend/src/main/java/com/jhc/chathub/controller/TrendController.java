package com.jhc.chathub.controller;

import com.jhc.chathub.common.request.CursorPageBaseReq;
import com.jhc.chathub.common.resp.CursorPageBaseResp;
import com.jhc.chathub.common.resp.Response;
import com.jhc.chathub.pojo.dto.talk.CreateTalkDTO;
import com.jhc.chathub.pojo.dto.talk.LikeInfoDTO;
import com.jhc.chathub.pojo.vo.TalkVO;
import com.jhc.chathub.service.ITrendService;
import com.jhc.chathub.utils.UserHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/11
 */
@RestController
@RequestMapping("/trend")
@Tag(name = "动态相关接口")
public class TrendController {
    @Resource
    private ITrendService trendService;

    @PostMapping("/talk")
    @Operation(summary = "发布说说")
    public Response<TalkVO> createTalk(@RequestBody CreateTalkDTO talk) {
        Long userId = UserHolder.getUser().getId();
        return Response.success(trendService.createTalk(userId, talk));
    }

    @GetMapping("/talk/list")
    @Operation(summary = "分页获取说说列表")
    public Response<CursorPageBaseResp<TalkVO>> getTalkList(CursorPageBaseReq req) {
        Long userId = UserHolder.getUser().getId();
        return Response.success(trendService.getTalkPage(userId, req));
    }

    @PostMapping("/talk/like")
    @Operation(summary = "点赞或取消点赞")
    public Response<Void> likeTalk(@RequestBody LikeInfoDTO likeInfo) {
        Long userId = UserHolder.getUser().getId();
        if (likeInfo.getIsLike()) {
            trendService.likeTalk(userId, likeInfo.getTalkId());
        } else {
            trendService.cancelLikeTalk(userId, likeInfo.getTalkId());
        }
        return Response.success(null);
    }
}
