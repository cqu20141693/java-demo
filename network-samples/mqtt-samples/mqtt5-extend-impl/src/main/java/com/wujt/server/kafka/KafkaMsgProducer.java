package com.wujt.server.kafka;

import com.alibaba.fastjson.JSON;
import com.wujt.server.kafka.config.KafkaConfiguration;
import com.wujt.server.kafka.config.KafkaTopicConfiguration;
import com.wujt.server.model.ChannelEventInfo;
import com.wujt.server.model.PublishInfo;
import com.wujt.server.mqtt.domain.evnet.QosEventInfo;
import com.wujt.server.spi.EventProcessor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * @author wujt
 */
@Component
public class KafkaMsgProducer implements KafkaAbstractProducer, EventProcessor<RecordMetadata> {
    private static final Logger logger = LoggerFactory.getLogger(KafkaMsgProducer.class);

    @Autowired
    private KafkaConfiguration kafkaConfiguration;

    @Autowired
    private KafkaProducerClient kafkaProducerClient;

    @Autowired
    private KafkaTopicConfiguration kafkaTopicConfiguration;

    @Override
    public CompletableFuture<RecordMetadata> sendMsg(String topic, String deviceKey, Object msg, boolean needAsync) {
        if (kafkaConfiguration.isEnable()) {
            String message = JSON.toJSONString(msg);
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, deviceKey, message);
            return kafkaProducerClient.send(record, needAsync);
        }
        logger.info("消息通知没有使用kafka 配置");
        return null;
    }

    @Override
    public CompletableFuture<RecordMetadata> sendQosEvent(String deviceKey, QosEventInfo msg, boolean needAsync) {
        return sendMsg(kafkaTopicConfiguration.getQosEvent(), deviceKey, msg, needAsync);
    }

    @Override
    public CompletableFuture<RecordMetadata> sendChannelEvent(String deviceKey, ChannelEventInfo msg, boolean needAsync) {
        return sendMsg(kafkaTopicConfiguration.getChannelConnEvent(), deviceKey, msg, needAsync);
    }

    @Override
    public CompletableFuture<RecordMetadata> sendPublishEvent(String deviceKey, PublishInfo msg, Boolean needAsync) {
        return sendMsg(kafkaTopicConfiguration.getOriginData(), deviceKey, msg, needAsync);
    }
}
