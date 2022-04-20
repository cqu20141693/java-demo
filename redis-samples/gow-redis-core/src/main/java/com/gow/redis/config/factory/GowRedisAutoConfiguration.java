package com.gow.redis.config.factory;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.core.RedisOperations;

/**
 * @author wujt  2021/6/3
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(GowRedisProperties.class)
public class GowRedisAutoConfiguration extends GowRedisConnectionConfiguration {


    public GowRedisAutoConfiguration(GowRedisProperties properties,
                                     ObjectProvider<RedisSentinelConfiguration> sentinelConfigurationProvider,
                                     ObjectProvider<RedisClusterConfiguration> clusterConfigurationProvider) {
        super(properties, sentinelConfigurationProvider, clusterConfigurationProvider);

    }

    @Bean
    @ConditionalOnMissingBean
    public GowStringRedisTemplate gowStringRedisTemplate(LettuceFactoryProxy lettuceFactoryProxy) {
        GowStringRedisTemplate template = new GowStringRedisTemplate();
        template.setConnectionFactory(lettuceFactoryProxy.getRedisConnectionFactory());
        return template;
    }


}
