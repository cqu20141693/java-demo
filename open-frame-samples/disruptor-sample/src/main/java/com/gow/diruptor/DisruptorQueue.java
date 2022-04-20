package com.gow.diruptor;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.List;

/**
 * @author gow 2021/05/09
 */
public class DisruptorQueue<T> {
    private Disruptor<MessageEvent<T>> disruptor;
    private RingBuffer<MessageEvent<T>> ringBuffer;
    private final EventTranslatorOneArg<MessageEvent<T>, T> TRANSLATOR =
            (event, sequence, message) -> event.setMessage(message);

    public DisruptorQueue(Disruptor<MessageEvent<T>> disruptor) {
        this.disruptor = disruptor;
        this.ringBuffer = disruptor.getRingBuffer();
        this.disruptor.start();
    }

    public void addWithTranslator(T message) {
        if (message != null) {
            ringBuffer.publishEvent(TRANSLATOR, message);
        }
    }

    public void add(T message) {
        if (message != null) {
            long sequence = this.ringBuffer.next();

            try {
                MessageEvent<T> event = this.ringBuffer.get(sequence);
                event.setMessage(message);
            } finally {
                this.ringBuffer.publish(sequence);
            }
        }
    }

    public void addAll(List<T> messages) {
        if (messages != null) {

            for (T t : messages) {
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

