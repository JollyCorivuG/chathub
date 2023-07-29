package com.jhc.chathub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jhc.chathub.pojo.entity.Message;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageMapper extends BaseMapper<Message> {
}
