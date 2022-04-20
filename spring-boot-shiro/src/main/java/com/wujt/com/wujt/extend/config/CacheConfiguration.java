package com.wujt.com.wujt.extend.config;

import lombok.Data;
import org.crazycake.shiro.BaseRedisManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSentinelManager;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wujt
 */
@Configuration
@ConfigurationProperties("shiro.redis")
@Data
@ConditionalOnProperty(prefix = "shiro", name = "cache", havingValue = "true")
public class CacheConfiguration {
    private String host;
    private int port;
    private int database;
    private String password;

    /**
     * 缓存过期时间
     */
    private int expire;
    /**
     * 连接超时时间
     */
    private int timeOut;

    @Value("${shiro.globalSessionTimeout}")
    private int globalSessionTimeout;

    @Value("${shiro.redis.sentinel.nodes}")
    private String nodes;

    @Value("${shiro.redis.sentinel.master}")
    private String master;

    @Value("${shiro.redis.sentinel.open}")
    private boolean sentinelOpen;

    /**
     * 配置shiro redisManager 使用的是shiro-redis开源插件
     *
     * @return
     */
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        redisManager.setDatabase(database);
        if (!StringUtils.isBlank(password)) {
            redisManager.setPassword(password);
        }
        // redis连接时间
        redisManager.setTimeout(timeOut);
        redisManager.setExpire(expire);
        return redisManager;
    }

    /**
     * 配置shiro redisSentinelManager   哨兵模式
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    public RedisSentinelManager redisSentinelManager() {
        RedisSentinelManager redisSentinelManager = new RedisSentinelManager();
        redisSentinelManager.setMasterName(master);
        redisSentinelManager.setHost(nodes);
        redisSentinelManager.setPassword(password);
        redisSentinelManager.setExpire(expire);
        return redisSentinelManager;
    }

    @Bean
    public BaseRedisManager baseRedisManager() {
        if (sentinelOpen) {
            return redisSentinelManager();
        } else {
            return redisManager();
        }
    }

}
