package bupt.edu.jhc.chathub.server.trend.mapper;

import bupt.edu.jhc.chathub.server.trend.domain.entity.Talk;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: 说说 mapper
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/22
 */
@Mapper
public interface TalkMapper extends BaseMapper<Talk> {
}
