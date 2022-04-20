package com.wujt.disruptor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ThreadFactory;

/**
 * @author wujt  2021/5/8
 */
public class DisruptorQueueFactory {

    /**
     * 创建"点对点模式"的操作队列，即同一事件会被一组消费者其中之一消费
     *
     * @param eventFactory
     * @param queueSize      大小（即队列大小）必须是 2 的 N 次方，实际项目中我们通常将其设置为 1024 * 1024
     * @param isMoreProducer
     * @param consumers
     * @param <T>
     * @return
     */
    public static <T> DisruptorQueue<T> getWorkPoolQueue(EventFactory<MessageEvent<T>> eventFactory, int queueSize, boolean isMoreProducer,
                                                         WaitStrategy waitStrategy, DisruptorConsumer<T>... consumers) {
        Disruptor<MessageEvent<T>> disruptor = getDisruptor(eventFactory, queueSize, isMoreProducer, waitStrategy);
        disruptor.handleEventsWithWorkerPool(consumers);
        DisruptorExceptionHandler exceptionHandler = new DisruptorExceptionHandler();
        disruptor.setDefaultExceptionHandler(exceptionHandler);
        return new DisruptorQueue<>(disruptor);
    }


    /**
     * 创建"发布订阅模式"的操作队列，即同一事件会被多个消费者并行消费
     *
     * @param eventFactory
     * @param queueSize
     * @param isMoreProducer
     * @param consumers
     * @param <T>
     * @return
     */
    public static <T> DisruptorQueue<T> getHandleEventsQueue(EventFactory<MessageEvent<T>> eventFactory, int queueSize, boolean isMoreProducer,
                                                             WaitStrategy waitStrategy, DisruptorConsumer<T>... consumers) {
        Disruptor<MessageEvent<T>> disruptor = getDisruptor(eventFactory, queueSize, isMoreProducer, waitStrategy);
        disruptor.handleEventsWith(consumers);
        DisruptorExceptionHandler exceptionHandler = new DisruptorExceptionHandler();
        disruptor.setDefaultExceptionHandler(exceptionHandler);
        return new DisruptorQueue<>(disruptor);
    }

    private static <T> Disruptor<MessageEvent<T>> getDisruptor(EventFactory<MessageEvent<T>> eventFactory, int ringBufferSize, boolean isMoreProducer, WaitStrategy waitStrategy) {
        ThreadFactory producerThreadFactory = new ThreadFactoryBuilder().setNameFormat("disruptor-producer-%d").setDaemon(true).build();
        return new Disruptor<>(eventFactory,
                ringBufferSize, producerThreadFactory,
                isMoreProducer ? ProducerType.MULTI : ProducerType.SINGLE,
                waitStrategy);
    }

    /**
     * 直接通过传入的 Disruptor 对象创建操作队列（如果消费者有依赖关系的话可以用此方法）
     *
     * @param disruptor
     * @param <T>
     * @return
     */
    public static <T> DisruptorQueue<T> getQueue(Disruptor<MessageEvent<T>> disruptor) {
        return new DisruptorQueue<>(disruptor);
    }
}
