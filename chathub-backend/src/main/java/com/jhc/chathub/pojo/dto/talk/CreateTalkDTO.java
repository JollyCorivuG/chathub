package com.jhc.chathub.pojo.dto.talk;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/11
 */
@Data
@Accessors(chain = true)
public class CreateTalkDTO {
    private String content;
    private List<TalkExtra> extra;
}
