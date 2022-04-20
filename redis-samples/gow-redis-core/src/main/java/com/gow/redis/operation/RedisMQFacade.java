package com.gow.redis.operation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * 发布订阅
 *
 * @author wujt  2021/5/14
 */
@Component
@Slf4j
public class RedisMQFacade extends RedisClient {

    @Autowired
    public RedisMQFacade(StringRedisTemplate template) {
        super(template);
    }

    @Autowired
    private MessageListener messageListener;
    @Autowired
    private RedisMessageListenerContainer container;

    @Autowired
    StreamMessageListenerContainer streamContainer;

    /**
     * pub message :  if you don’t listen, you miss a message
     *
     * @param topic
     * @param message
     */
    public void publish(String topic, String message) {
        template.convertAndSend(topic, message);
    }

    /**
     * 添加 pattern listen : Pub/Sub registers a server-side subscription, server push
     *
     * @param topic
     * @date 2021/5/14 10:12
     */
    public void addPatternTopic(String topic) {
        container.addMessageListener(messageListener, new PatternTopic(topic));
        log.info("add redis listener on pattern topic {}", topic);
    }

    /**
     * 删除pattern listen
     *
     * @param topic
     * @date 2021/5/14 10:12
     */
    public void removePatternTopic(String topic) {
        container.removeMessageListener(messageListener, new PatternTopic(topic));
        log.info("remove redis listener on pattern topic {}", topic);
    }

    /**
     * 添加 channel listen
     *
     * @param topic
     * @date 2021/5/14 10:12
     */
    public void addChannelTopic(String topic) {
        container.addMessageListener(messageListener, new ChannelTopic(topic));
        log.info("add redis listener on channel topic {}", topic);
    }

    /**
     * 删除 channel listen
     *
     * @param topic
     * @date 2021/5/14 10:12
     */
    public void removeChannelTopic(String topic) {
        container.removeMessageListener(messageListener, new ChannelTopic(topic));
        log.info("remove redis listener on channel topic {}", topic);
    }


    // stream

    /**
     * create stream group
     *
     * @param key
     * @param group
     * @return
     */
    public String createGroup(String key, String group) {
        return template.opsForStream().createGroup(key, group);
    }

    /**
     * append log to stream key  until the stream is trimmed
     *
     * @param stringRecord
     * @return
     */
    public RecordId appendRecord(StringRecord stringRecord) {
        return template.opsForStream().add(stringRecord);
    }

    /**
     * consumer stream log
     *
     * @param consumer
     * @param streamReadOptions
     * @param streamOffset
     * @param action
     */
    public void consumer(Consumer consumer, StreamReadOptions streamReadOptions, StreamOffset streamOffset,
                         java.util.function.Consumer<? super MapRecord> action) {

        template.opsForStream().read(consumer,
                streamReadOptions,
                streamOffset).forEach(record -> {
            MapRecord mapRecord = (MapRecord) record;
            action.accept(mapRecord);
            template.opsForStream().acknowledge((String) streamOffset.getKey(), consumer.getGroup(), mapRecord.getId());
        });
    }

    /**
     * add stream subscription to stream container
     *
     * @param consumer
     * @param streamOffset
     * @param listener
     */
    public void addConsumer(Consumer consumer, StreamOffset streamOffset, StreamListener<String, StringRecord> listener) {
        streamContainer.receiveAutoAck(consumer, streamOffset, listener);
    }

}
