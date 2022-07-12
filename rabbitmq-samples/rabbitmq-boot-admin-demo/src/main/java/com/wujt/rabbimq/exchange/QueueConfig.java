package com.wujt.rabbimq.exchange;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 交换机队列
 * wcc 2022/7/11
 */
@Configuration
public class QueueConfig {

    public final static String USER_QUEUE = "userQueue";
    public final static String STRING_QUEUE = "strQueue";

    @Bean(name = USER_QUEUE)
    public Queue queueMessages() {
        //String name : 需要创建的队列名称
        // boolean durable：
        // boolean exclusive：
        // boolean autoDelete
        return new Queue(USER_QUEUE, true, false, false);
    }

    /**
     * 定义队列(string)
     *
     * @return
     */
    @Bean(STRING_QUEUE)
    public Queue queueStr() {
        return new Queue(STRING_QUEUE);
    }

}
