package com.wujt.rabbitmq.input;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 消息接受
 * wcc 2022/7/11
 */
public interface MessageSink {
    /**
     * Input channel name.
     */
    String INPUT = "/data/exchange/8b7rHVjk";

    /**
     * 消费者信道
     */
    @Input(MessageSink.INPUT)
    SubscribableChannel input();
}
