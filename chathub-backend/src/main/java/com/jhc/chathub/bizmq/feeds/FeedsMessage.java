package com.jhc.chathub.bizmq.feeds;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/12
 */
@Data
@Accessors(chain = true)
public class FeedsMessage {
    private Long authorId;
    private Long talkId;
    public static boolean isValid(FeedsMessage feedsMessage) {
        return feedsMessage != null && feedsMessage.getAuthorId() != null && feedsMessage.getTalkId() != null;
    }
}
