package bupt.edu.jhc.chathub.server.chat.mapper;

import bupt.edu.jhc.chathub.server.chat.domain.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: 消息 mapper
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}
