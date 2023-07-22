package com.jhc.chathub.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("tb_user")
public class User {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String account;
    private String password;
    private String phone;
    private String nickName;
    private String avatarUrl;
    private Integer level;
    private Integer friendCount;
    private Integer groupCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
