package com.jhc.chathub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jhc.chathub.pojo.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/11
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
