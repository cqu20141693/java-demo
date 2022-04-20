package com.wujt.com.wujt.shiro.config;

import org.apache.shiro.mgt.SessionsSecurityManager;
import org.crazycake.shiro.BaseRedisManager;
import org.crazycake.shiro.RedisCacheManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wujt
 */
@Configuration
@ConditionalOnBean(BaseRedisManager.class)
public class RedisConfiger {

    @Bean
    public RedisCacheManager cacheManager(BaseRedisManager redisManager, SessionsSecurityManager securityManager) {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager);
        redisCacheManager.setExpire(redisManager.getExpire());
        // 配置缓存管理器（授权信息缓存默认开启，认证信息缓存默认关闭）
        securityManager.setCacheManager(redisCacheManager);
        return redisCacheManager;
    }
}
