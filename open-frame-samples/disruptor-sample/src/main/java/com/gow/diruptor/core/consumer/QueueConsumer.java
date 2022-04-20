package com.gow.diruptor.core.consumer;

import com.gow.diruptor.core.event.DataEvent;
import com.lmax.disruptor.WorkHandler;

import java.util.concurrent.ExecutorService;

/**
 * @author gow 2021/05/11
 */
public class QueueConsumer<T> implements WorkHandler<DataEvent<T>> {

    private ExecutorService executor;

    private QueueConsumerFactory<T> factory;

    /**
     * Instantiates a new Queue consumer.
     *
     * @param executor the executor
     * @param factory the factory
     */
    public QueueConsumer(final ExecutorService executor, final QueueConsumerFactory<T> factory) {
        this.executor = executor;
        this.factory = factory;
    }

    @Override
    public void onEvent(final DataEvent<T> t) {
        if (t != null) {
            QueueConsumerExecutor<T> queueConsumerExecutor = factory.create();
            queueConsumerExecutor.setData(t.getData());
            executor.execute(queueConsumerExecutor);
        }
    }
}