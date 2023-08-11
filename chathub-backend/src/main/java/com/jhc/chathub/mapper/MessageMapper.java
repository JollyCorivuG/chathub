package com.jhc.chathub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jhc.chathub.pojo.entity.Message;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}
