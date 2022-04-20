package com.gow.redis.config;

import com.gow.redis.support.DefaultRedisMessageHandler;
import com.gow.redis.support.MessageHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * @author wujt  2021/5/14
 */
@Configuration
public class RedisMQConfiguration {

    @Bean
    @ConditionalOnMissingBean(MessageHandler.class)
    public MessageHandler messageHandler() {
        return new DefaultRedisMessageHandler();
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(MessageHandler messageHandler) {

        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter();
        messageListenerAdapter.setDelegate(messageHandler);
        messageListenerAdapter.afterPropertiesSet();
        return messageListenerAdapter;
    }

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(StringRedisTemplate redisTemplate,
                                                                @Qualifier("redisMQExecutor") Executor redisMQExecutor) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        container.setTaskExecutor(redisMQExecutor);
        container.setRecoveryInterval(1000);
        container.afterPropertiesSet();
        return container;
    }

    @Bean
    public StreamMessageListenerContainer streamMessageListenerContainer(RedisConnectionFactory redisConnectionFactory, StringRedisTemplate streamRedisTemplate) {

        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> containerOptions = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                .builder().pollTimeout(Duration.ofMillis(100)).build();

        StreamMessageListenerContainer<String, MapRecord<String, String, String>> container = StreamMessageListenerContainer.create(redisConnectionFactory,
                containerOptions);
        container.start();

        return container;
    }
}
