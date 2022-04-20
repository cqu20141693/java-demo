package com.gow.pulsar.listener;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageListener;

/**
 * Cannot use receive() when a listener has been set
 *
 * @author gow
 * @date 2021/7/6
 */
@Slf4j
public class IMessageListener<V> implements MessageListener<V> {
    @SneakyThrows
    @Override
    public void received(Consumer<V> consumer, Message<V> msg) {
        log.info("consumer name={},topic={} receive msg,key={},value={},messageId={},pubTime={},eventTime={}",
                consumer.getConsumerName(),
                consumer.getTopic(), msg.getKey(), JSONObject.toJSONString(msg.getValue()), msg.getMessageId(),
                msg.getPublishTime(),
                msg.getEventTime());
        consumer.acknowledge(msg);
    }

    @Override
    public void reachedEndOfTopic(Consumer<V> consumer) {
        log.info("consumer name={},topic={} reachedEndOfTopic, no more messages are coming ",
                consumer.getConsumerName(), consumer.getTopic());
        MessageListener.super.reachedEndOfTopic(consumer);
    }
}
