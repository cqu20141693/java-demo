package com.wujt.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wujt
 * @version 1.0
 * @date 2020/3/23
 */
@Configuration
public class QueueConfig {

    public static final String topicName = "wujt.user";

    @Bean(name = "messages")
    public Queue queueMessages() {
        //String name : 需要创建的队列名称
        // boolean durable：
        // boolean exclusive：
        // boolean autoDelete
        return new Queue(topicName, true, false, false);
    }
}
