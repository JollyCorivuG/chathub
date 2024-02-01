package bupt.edu.jhc.chathub.server.trend.mapper;

import bupt.edu.jhc.chathub.server.trend.domain.entity.Like;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: 点赞 mapper
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/22
 */
@Mapper
public interface LikeMapper extends BaseMapper<Like> {
}
