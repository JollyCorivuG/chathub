package com.jhc.chathub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.chathub.common.request.CursorPageBaseReq;
import com.jhc.chathub.common.resp.CursorPageBaseResp;
import com.jhc.chathub.pojo.dto.talk.CreateTalkDTO;
import com.jhc.chathub.pojo.entity.Talk;
import com.jhc.chathub.pojo.vo.TalkVO;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/11
 */
public interface ITrendService extends IService<Talk> {
    TalkVO convertTalkToVO(Talk talk);

    TalkVO createTalk(Long userId, CreateTalkDTO talk);

    CursorPageBaseResp<TalkVO> getTalkPage(Long userId, CursorPageBaseReq req);

    void likeTalk(Long userId, Long talkId);

    void cancelLikeTalk(Long userId, Long talkId);
}
