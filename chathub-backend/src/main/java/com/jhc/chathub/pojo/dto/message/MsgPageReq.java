package com.jhc.chathub.pojo.dto.message;

import com.jhc.chathub.common.request.CursorPageBaseReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MsgPageReq extends CursorPageBaseReq {
    private Long roomId;
}
