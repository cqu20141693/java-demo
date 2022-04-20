package com.wujt.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wujt
 * @version 1.0
 * @date 2020/3/23
 */
@Configuration
public class ExchangeConfig {


    public static String exchangName = "topic-exchange";
    public static String routingKey = "wujt.user";
    public static String topicRoutingKey = "wujt.#";

    /**
     * 定义交换机
     *
     * @return
     */
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchangName);
    }

    @Bean
    Binding bindingExchangeMessages(@Qualifier("messages") Queue queueMessages, TopicExchange exchange) {
        // 绑定消息到交换机并按照routingKey 进行路由
        return BindingBuilder.bind(queueMessages).to(exchange).with(topicRoutingKey);
    }

}
