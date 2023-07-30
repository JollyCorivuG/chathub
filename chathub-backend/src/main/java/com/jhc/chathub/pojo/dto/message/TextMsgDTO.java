package com.jhc.chathub.pojo.dto.message;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TextMsgDTO {
    private String content;
}
