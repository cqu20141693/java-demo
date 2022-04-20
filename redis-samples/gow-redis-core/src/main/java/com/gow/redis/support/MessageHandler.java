package com.gow.redis.support;

/**
 * @author wujt  2021/5/14
 */
public interface MessageHandler {
    /**
     * 处理redis 订阅消息
     *
     * @param message
     * @param channel
     * @date 2021/5/14 9:47
     */
    void handleMessage(String message, String channel);
}
