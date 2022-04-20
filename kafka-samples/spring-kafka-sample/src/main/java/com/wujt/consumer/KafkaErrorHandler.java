package com.wujt.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.listener.BatchErrorHandler;

/**
 * @author wujt  2021/4/29
 */
public class KafkaErrorHandler implements BatchErrorHandler {
    @Override
    public void handle(Exception thrownException, ConsumerRecords<?, ?> data) {

    }
}
