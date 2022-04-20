package com.gow.redis.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/6/24
 */
@Component
public class RedisStreamFacade extends RedisClient {

    @Autowired
    public RedisStreamFacade(StringRedisTemplate template) {
        super(template);
    }
}
