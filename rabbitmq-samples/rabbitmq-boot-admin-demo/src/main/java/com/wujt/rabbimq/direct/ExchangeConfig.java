package com.wujt.rabbimq.direct;

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


    public final static String DIRECT_EXCHANGE = "cc-direct-exchange";
    public final static String routingKey = "cc.user";

    public static String topicRoutingKey = "cc.#";

    /**
     * 定义交换机
     *
     * @return
     */
    @Bean(name = DIRECT_EXCHANGE)
    public TopicExchange exchange() {
        return new TopicExchange(DIRECT_EXCHANGE,false,true);
    }

    /**
     * 创建绑定
     *
     * @param queueMessages
     * @param exchange
     * @return
     */
    @Bean
    Binding bindingExchangeMessages(@Qualifier("userQueue") Queue queueMessages, @Qualifier(DIRECT_EXCHANGE) TopicExchange exchange) {
        // 绑定消息到交换机并按照routingKey 进行路由
        return BindingBuilder.bind(queueMessages).to(exchange).with(topicRoutingKey);
    }

}
