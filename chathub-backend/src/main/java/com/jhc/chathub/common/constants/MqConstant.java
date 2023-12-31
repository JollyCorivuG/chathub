package com.jhc.chathub.common.constants;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/12
 */
public class MqConstant {
    // 跟feeds流相关消息队列的常量
    public static final String FEEDS_EXCHANGE = "feeds_exchange";
    public static final String FEEDS_QUEUE = "feeds_queue";
    public static final String FEEDS_ROUTING_KEY = "feeds_routing_key";

    // 跟点赞相关消息队列的常量
    public static final String LIKE_EXCHANGE = "like_exchange";
    public static final String LIKE_QUEUE = "like_queue";
    public static final String LIKE_ROUTING_KEY = "like_routing_key";
}
