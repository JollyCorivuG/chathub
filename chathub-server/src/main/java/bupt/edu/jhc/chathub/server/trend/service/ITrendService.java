package bupt.edu.jhc.chathub.server.trend.service;

import bupt.edu.jhc.chathub.common.domain.vo.request.CursorPageBaseReq;
import bupt.edu.jhc.chathub.common.domain.vo.resp.CursorPageBaseResp;
import bupt.edu.jhc.chathub.server.trend.domain.dto.talk.CreateTalkDTO;
import bupt.edu.jhc.chathub.server.trend.domain.entity.Talk;
import bupt.edu.jhc.chathub.server.trend.domain.vo.TalkVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 动态服务接口
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/22
 */
public interface ITrendService extends IService<Talk> {
    TalkVO convertTalkToVO(Talk talk);

    TalkVO createTalk(Long userId, CreateTalkDTO talk);

    CursorPageBaseResp<TalkVO> getTalkPage(Long userId, CursorPageBaseReq req);

    void likeTalk(Long userId, Long talkId);

    void cancelLikeTalk(Long userId, Long talkId);
}
