package com.gow.redis.support;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wujt  2021/5/14
 */
@Slf4j
public class DefaultRedisMessageHandler implements MessageHandler {

    @Override
    public void handleMessage(String message, String channel) {
        log.info("receive msg={} from chan={}", message, channel);
    }
}
