package com.jhc.chathub.controller;

import com.jhc.chathub.common.resp.Response;
import com.jhc.chathub.pojo.dto.talk.CreateTalkDTO;
import com.jhc.chathub.pojo.vo.TalkVO;
import com.jhc.chathub.service.ITrendService;
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
}
