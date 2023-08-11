package com.jhc.chathub.pojo.dto.talk;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/11
 */
@Data
@Accessors(chain = true)
public class TalkExtra {
    private Integer type;
    private Object data;
}
