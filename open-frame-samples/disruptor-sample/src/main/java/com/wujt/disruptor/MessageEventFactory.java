package com.wujt.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * @author wujt  2021/5/8
 */
public class MessageEventFactory<T> implements EventFactory<MessageEvent<T>> {

    @Override
    public MessageEvent<T> newInstance() {
        return new MessageEvent<>();
    }
}
