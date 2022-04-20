package com.gow.diruptor;

import com.lmax.disruptor.EventFactory;

/**
 * @author gow 2021/05/09
 */
public class MessageEventFactory<T> implements EventFactory<MessageEvent<T>> {
    @Override
    public MessageEvent<T> newInstance() {
        return new MessageEvent<>();
    }
}
