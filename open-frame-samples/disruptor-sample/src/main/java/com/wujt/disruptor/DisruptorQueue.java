package com.wujt.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.List;

/**
 * @author wujt  2021/5/8
 */
public class DisruptorQueue<T> {
    private Disruptor<MessageEvent<T>> disruptor;
    private RingBuffer<MessageEvent<T>> ringBuffer;

    public DisruptorQueue(Disruptor<MessageEvent<T>> disruptor) {
        this.disruptor = disruptor;
        this.ringBuffer = disruptor.getRingBuffer();
        this.disruptor.start();
    }

    public void add(T t) {
        if (t != null) {
            long sequence = this.ringBuffer.next();

            try {
                MessageEvent<T> event = this.ringBuffer.get(sequence);
                event.setMessage(t);
            } finally {
                this.ringBuffer.publish(sequence);
            }
        }
    }

    public void addAll(List<T> ts) {
        if (ts != null) {

            for (T t : ts) {
                if (t != null) {
                    this.add(t);
                }
            }
        }
    }

    public long cursor() {
        return this.disruptor.getRingBuffer().getCursor();
    }

    public void shutdown() {
        this.disruptor.shutdown();
    }

    public Disruptor<MessageEvent<T>> getDisruptor() {
        return this.disruptor;
    }

    public void setDisruptor(Disruptor<MessageEvent<T>> disruptor) {
        this.disruptor = disruptor;
    }

    public RingBuffer<MessageEvent<T>> getRingBuffer() {
        return this.ringBuffer;
    }

    public void setRingBuffer(RingBuffer<MessageEvent<T>> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }
}
