package com.wujt.rabbitmq.input;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

import java.util.Date;

/**
 * 消费者
 * wcc 2022/7/11
 */
@EnableBinding(MessageSink.class)
public class MessageConsumer {
    @StreamListener(MessageSink.INPUT)
    public void input(Message message) {
        System.out.println("消息接收：<" + message.getPayload() + "> 完成，时间：" + new Date());
    }
}
