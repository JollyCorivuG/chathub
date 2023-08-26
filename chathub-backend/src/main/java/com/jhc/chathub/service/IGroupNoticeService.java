package com.jhc.chathub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.chathub.pojo.entity.GroupNotice;
import com.jhc.chathub.pojo.vo.GroupNoticeVO;

import java.util.List;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/25
 */
public interface IGroupNoticeService extends IService<GroupNotice> {
    List<GroupNoticeVO> getGroupNotice(Long userId);
}
