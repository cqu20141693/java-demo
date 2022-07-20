package com.wujt.rabbimq.topic;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.wujt.rabbimq.exchange.QueueConfig.STRING_QUEUE;
import static com.wujt.rabbimq.exchange.QueueConfig.USER_QUEUE;

/**
 * topic exchange 配置
 * 路由键匹配：先将路由键、bindings键根据点号隔开，# 表示匹配 0 个或多个单词， “*”表示匹配一个单词
 * wcc 2022/7/11
 */
@Configuration
public class TopicExchangeConfig {
    public final static String TOPIC_EXCHANGE = "cc-topic-exchange";
    public static final String ROUTE_KEY_USER = "rabbitmq.spring.boot.#";
    public static final String ROUTE_KEY_STR = "rabbitmq.str.#";

    /**
     * 定义主题交换机(demo)
     *
     * @return
     */
    @Bean(TOPIC_EXCHANGE)
    public TopicExchange topicExchangeDemo() {
        return new TopicExchange(TOPIC_EXCHANGE, false, true);
    }

    /**
     * 绑定主题交换机与队列(demo)
     *
     * @param queue
     * @param topicExchange
     * @return
     */
    @Bean
    public Binding bindingTopicExchangeDemo(@Qualifier(USER_QUEUE) Queue queue,
                                            @Qualifier(TOPIC_EXCHANGE) TopicExchange topicExchange) {
        return BindingBuilder.bind(queue).to(topicExchange).with(ROUTE_KEY_USER);
    }

    /**
     * 绑定主题交换机与队列(demo)
     *
     * @param queue
     * @param topicExchange
     * @return
     */
    @Bean
    public Binding bindingTopicExchangeStr(@Qualifier(STRING_QUEUE) Queue queue,
                                           @Qualifier(TOPIC_EXCHANGE) TopicExchange topicExchange) {
        return BindingBuilder.bind(queue).to(topicExchange).with(ROUTE_KEY_STR);
    }
}
