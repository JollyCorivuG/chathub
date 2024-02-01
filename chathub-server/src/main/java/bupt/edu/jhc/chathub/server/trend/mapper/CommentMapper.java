package bupt.edu.jhc.chathub.server.trend.mapper;

import bupt.edu.jhc.chathub.server.trend.domain.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: 评论 mapper
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/22
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
