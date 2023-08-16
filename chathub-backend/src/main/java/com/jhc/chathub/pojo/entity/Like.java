package com.jhc.chathub.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/16
 */
@Data
@Accessors(chain = true)
@TableName("tb_like")
public class Like {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long talkId;
    private Long userId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
