package com.wujt.server.kafka;

import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.concurrent.CompletableFuture;

/**
 * @author wujt
 */
public interface KafkaAbstractProducer {
    /**
     * 发送kafka
     *
     * @param topic
     * @param key       link deviceKey
     * @param msg
     * @param needAsync
     * @return
     */
    CompletableFuture<RecordMetadata> sendMsg(String topic, String key, Object msg, boolean needAsync);
}
