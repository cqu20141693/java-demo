package com.wujt.rabbimq.fanout;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.wujt.rabbimq.exchange.QueueConfig.STRING_QUEUE;
import static com.wujt.rabbimq.exchange.QueueConfig.USER_QUEUE;

/**
 * 广播交换机配置
 * wcc 2022/7/11
 */
@Configuration
public class FanoutExchangeConfig {
    public final static String FANOUT_EXCHANGE = "cc-fanout-exchange";


    /**
     * 定义广播交换机
     *
     * @return
     */
    @Bean(FANOUT_EXCHANGE)
    public FanoutExchange fanoutExchangeDemo() {
        return new FanoutExchange(FANOUT_EXCHANGE, false, true);
    }

    /**
     * 绑定广播交换机与队列(demo)
     *
     * @param queue
     * @param fanoutExchange
     * @return
     */
    @Bean
    public Binding bingingFanoutExchange(@Qualifier(USER_QUEUE) Queue queue,
                                         @Qualifier(FANOUT_EXCHANGE) FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }
    /**
     * 绑定广播交换机与队列(demo)
     *
     * @param queue
     * @param fanoutExchange
     * @return
     */
    @Bean
    public Binding bingingFanoutExchangeStr(@Qualifier(STRING_QUEUE) Queue queue,
                                         @Qualifier(FANOUT_EXCHANGE) FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }
}
