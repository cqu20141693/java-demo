package com.gow.redis.operation;

import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author wujt  2021/5/14
 */
public class RedisClient {
    public RedisClient() {
    }

    public RedisClient(StringRedisTemplate template) {
        this.template = template;
    }

    protected StringRedisTemplate template;

    public StringRedisTemplate getTemplate() {
        return template;
    }

    public void setTemplate(StringRedisTemplate template) {
        this.template = template;
    }

}
