package com.gow.app.config;

import com.gow.redis.support.MessageHandler;

/**
 * @author wujt  2021/5/14
 */
public class KeyExpiredEventHandler implements MessageHandler {
    @Override
    public void handleMessage(String message, String channel) {

    }
}
