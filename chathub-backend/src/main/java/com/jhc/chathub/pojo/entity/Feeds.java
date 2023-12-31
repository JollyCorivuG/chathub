package com.jhc.chathub.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/11
 */
@Data
@Accessors(chain = true)
@TableName("tb_feeds")
public class Feeds {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long talkId;
    private Long connectUserId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
