package bupt.edu.jhc.chathub.server.trend.controller;

import bupt.edu.jhc.chathub.common.domain.vo.request.CursorPageBaseReq;
import bupt.edu.jhc.chathub.common.domain.vo.resp.CursorPageBaseResp;
import bupt.edu.jhc.chathub.common.domain.vo.resp.Response;
import bupt.edu.jhc.chathub.common.utils.context.RequestHolder;
import bupt.edu.jhc.chathub.server.trend.domain.dto.talk.CreateTalkDTO;
import bupt.edu.jhc.chathub.server.trend.domain.dto.talk.LikeInfoDTO;
import bupt.edu.jhc.chathub.server.trend.domain.vo.TalkVO;
import bupt.edu.jhc.chathub.server.trend.service.ITrendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Description: 动态接口
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/22
 */
@RestController
@RequestMapping("/api/trend")
@Api(tags = "动态相关接口")
public class TrendController {
    @Resource
    private ITrendService trendService;

    @PostMapping("/talk")
    @ApiOperation("发布说说")
    public Response<TalkVO> createTalk(@RequestBody CreateTalkDTO talk) {
        Long userId = RequestHolder.get().getUid();
        return Response.success(trendService.createTalk(userId, talk));
    }

    @GetMapping("/talk/list")
    @ApiOperation("分页获取说说列表")
    public Response<CursorPageBaseResp<TalkVO>> getTalkList(CursorPageBaseReq req) {
        Long userId = RequestHolder.get().getUid();
        return Response.success(trendService.getTalkPage(userId, req));
    }

    @PostMapping("/talk/like")
    @ApiOperation("点赞或取消点赞")
    public Response<Void> likeTalk(@RequestBody LikeInfoDTO likeInfo) {
        Long userId = RequestHolder.get().getUid();
        if (likeInfo.getIsLike()) {
            trendService.likeTalk(userId, likeInfo.getTalkId());
        } else {
            trendService.cancelLikeTalk(userId, likeInfo.getTalkId());
        }
        return Response.success(null);
    }
}
