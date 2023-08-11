package com.jhc.chathub.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.jhc.chathub.pojo.dto.talk.TalkExtra;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/11
 */
@Data
@Accessors(chain = true)
@TableName(value = "tb_talk", autoResultMap = true)
public class Talk {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long authorId;
    private String content;
    @TableField(value = "extra", typeHandler = JacksonTypeHandler.class)
    private List<TalkExtra> extra = Collections.emptyList();
    private Integer likeCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
