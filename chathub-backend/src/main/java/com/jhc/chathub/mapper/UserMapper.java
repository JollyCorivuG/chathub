package com.jhc.chathub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jhc.chathub.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
