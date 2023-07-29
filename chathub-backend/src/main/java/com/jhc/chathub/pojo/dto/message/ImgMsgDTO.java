package com.jhc.chathub.pojo.dto.message;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ImgMsgDTO {
    private Long size;
    private Integer width;
    private Integer height;
    private String url;
}
