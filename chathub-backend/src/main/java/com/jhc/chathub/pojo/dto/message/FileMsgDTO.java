package com.jhc.chathub.pojo.dto.message;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FileMsgDTO {
    private Long size;
    private String url;
    private String fileName;
}
