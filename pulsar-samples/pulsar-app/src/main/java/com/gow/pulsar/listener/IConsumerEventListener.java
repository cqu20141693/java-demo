package com.gow.pulsar.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.ConsumerEventListener;

/**
 * consumer listener is only supported for failover subscription
 *
 * @author gow
 * @date 2021/7/6
 */
@Slf4j
public class IConsumerEventListener implements ConsumerEventListener {
    @Override
    public void becameActive(Consumer<?> consumer, int partitionId) {
        log.info("consumer name={},subscription={},topic={}, partitionId={} active", consumer.getConsumerName(),
                consumer.getSubscription(), consumer.getTopic(), partitionId);
    }

    @Override
    public void becameInactive(Consumer<?> consumer, int partitionId) {
        log.info("consumer name={},subscription={},topic={}, partitionId={} inactive", consumer.getConsumerName(),
                consumer.getSubscription(), consumer.getTopic(), partitionId);
    }
}