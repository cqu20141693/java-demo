package com.wujt.config;


import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.wujt.component.HighEventHandler;
import com.wujt.component.LowEventHandler;
import com.wujt.model.Element;
import com.gow.util.ElementEventFactory;
import com.gow.util.ProducerThreadFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wujt
 */
@Configuration
public class DisruptorConfig {


    @Bean
    public EventHandler<Element> lowEventHandler() {
        return new LowEventHandler();
    }

    @Bean
    public EventHandler<Element> highEventHandler() {
        return new HighEventHandler();
    }


    @Bean
    public Disruptor<Element> disruptor(@Qualifier("lowEventHandler") EventHandler<Element> lowHandler,
                                        @Qualifier("highEventHandler") EventHandler<Element> highHandler) {
        ElementEventFactory eventFactory = new ElementEventFactory();
        ProducerThreadFactory threadFactory = new ProducerThreadFactory("element");
        Disruptor<Element> elementDisruptor = new Disruptor<>(
                eventFactory
                , 1024
                , threadFactory
                , ProducerType.SINGLE
                , new YieldingWaitStrategy());
        elementDisruptor.handleEventsWith(highHandler)
                .thenHandleEventsWithWorkerPool(new LowEventHandler())
                .then(lowHandler);
        elementDisruptor.start();
        return elementDisruptor;
    }
}
