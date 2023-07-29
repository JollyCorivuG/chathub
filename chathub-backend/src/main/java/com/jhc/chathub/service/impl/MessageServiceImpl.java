package com.jhc.chathub.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.chathub.mapper.MessageMapper;
import com.jhc.chathub.pojo.entity.Message;
import com.jhc.chathub.service.IMessageService;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {
}
