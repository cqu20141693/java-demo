package com.wujt.disruptor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * @author wujt  2021/5/8
 */
public abstract class DisruptorConsumer<T>
        implements EventHandler<MessageEvent<T>>, WorkHandler<MessageEvent<T>> {

    @Override
    public void onEvent(MessageEvent<T> event, long sequence, boolean endOfBatch) {
        this.onEvent(event);
    }

    @Override
    public void onEvent(MessageEvent<T> event) {
        this.consume(event.getMessage());
    }

    /**
     *
     * @param message
     */
    public abstract void consume(T message);
}
