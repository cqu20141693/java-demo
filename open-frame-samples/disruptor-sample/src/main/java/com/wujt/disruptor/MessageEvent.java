package com.wujt.disruptor;

/**
 * @author wujt  2021/5/8
 * 消事件
 */
public class MessageEvent<T> {
    private T message;

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }
}
