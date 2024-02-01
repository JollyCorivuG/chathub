package bupt.edu.jhc.chathub.server.trend.domain.entity;

import bupt.edu.jhc.chathub.server.trend.domain.dto.talk.TalkExtra;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @Description: 说说实体类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/22
 */
@Data
@Accessors(chain = true)
@TableName(value = "tb_talk", autoResultMap = true)
public class Talk {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("author_id")
    private Long authorId;
    @TableField("content")
    private String content;
    @TableField(value = "extra", typeHandler = JacksonTypeHandler.class)
    private List<TalkExtra> extra = Collections.emptyList();
    @TableField("like_count")
    private Integer likeCount;
    @TableField("create_time")
    private LocalDateTime createTime;
    @TableField("update_time")
    private LocalDateTime updateTime;
}
