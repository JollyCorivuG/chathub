package com.jhc.chathub.service;

import com.baomidou.mybatisplus.extension.service.IService;
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
}
