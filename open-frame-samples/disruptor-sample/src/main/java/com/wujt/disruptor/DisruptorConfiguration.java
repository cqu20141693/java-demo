package com.wujt.disruptor;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.wujt.disruptor.sample.Log;
import com.wujt.disruptor.sample.LogConsumer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wujt  2021/5/8
 */
@Configuration
public class DisruptorConfiguration {

    @Bean
    @ConditionalOnMissingBean(WaitStrategy.class)
    public WaitStrategy waitStrategy() {
        return new SleepingWaitStrategy();
    }

    @Bean
    public EventFactory<MessageEvent<Log>> eventFactory() {
        return new MessageEventFactory<>();
    }

    @Bean
    public DisruptorQueue<Log> disruptorQueue(WaitStrategy waitStrategy, EventFactory<MessageEvent<Log>> eventFactory) {
        LogConsumer logConsumer = new LogConsumer();
        return DisruptorQueueFactory.getHandleEventsQueue(eventFactory, 1024 * 1024, true, waitStrategy, logConsumer);

    }

    @Bean(destroyMethod = "stop")
    public DisruptorProducer<Log> disruptorProducer(DisruptorQueue<Log> disruptorQueue) {
        return new DisruptorProducer<>(disruptorQueue);
    }

}
