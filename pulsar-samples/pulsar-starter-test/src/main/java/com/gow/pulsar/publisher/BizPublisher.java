package com.gow.pulsar.publisher;

import com.gow.pulsar.core.domain.ProducerRecord;
import com.gow.pulsar.core.producer.ProducerTemplate;
import com.gow.pulsar.core.producer.StringProducerTemplate;
import com.gow.pulsar.test.schema.model.RawDataV2;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.shade.org.apache.commons.lang3.RandomStringUtils;
import org.apache.pulsar.shade.org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/7/20
 */
@Component
@Slf4j
public class BizPublisher implements CommandLineRunner {

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private StringProducerTemplate stringProducerTemplate;

    private final String FAIL_OVER_TOPIC = "persistent://gow/persistent/p-failover-test-topic";
    private final String JSON_SCHEMA_TOPIC = "persistent://gow/persistent/json-schema-test-topic";
    private final String DELAY_EVENT_TOPIC = "persistent://gow/persistent/delay-event-topic";

    @Autowired
    private TaskScheduler taskScheduler;


    @Override
    public void run(String... args) throws Exception {
        producerTemplate.send("test-topic", "key", "value");
        Supplier<ProducerRecord<String>> stringSupplier =
                () -> new ProducerRecord<>(FAIL_OVER_TOPIC, RandomStringUtils.randomAlphabetic(8),
                        new Date().toString());
        Date date = new Date(System.currentTimeMillis());
        taskScheduler.scheduleWithFixedDelay(() -> {
            try {
                send(stringSupplier);
            } catch (PulsarClientException e) {
                e.printStackTrace();
            }
        }, date, 20);
        Supplier<ProducerRecord<RawDataV2>> rawSupplier =

                () -> {
                    RawDataV2 rawData = new RawDataV2();
                    rawData.setTopic(RandomStringUtils.randomAlphanumeric(8));
                    rawData.setType(RandomUtils.nextInt());
                    rawData.setData(RandomStringUtils.randomAlphanumeric(8).getBytes(
                            StandardCharsets.UTF_8));
                    rawData.setVersion("v2");
                    return new ProducerRecord<>(JSON_SCHEMA_TOPIC, LocalDateTime.now().toString(), rawData);

                };
        taskScheduler.scheduleWithFixedDelay(() -> sendAsync(rawSupplier), date, 5000);

        Supplier<ProducerRecord<String>> recordSupplier =
                () -> new ProducerRecord<>(DELAY_EVENT_TOPIC, RandomStringUtils.randomAlphabetic(8),
                        new Date().toString());
        taskScheduler.scheduleWithFixedDelay(() -> {
            try {
                sendDelay(recordSupplier);
            } catch (PulsarClientException e) {
                e.printStackTrace();
            }
        }, date, 5000);
    }

    private void sendDelay(Supplier<ProducerRecord<String>> supplier) throws PulsarClientException {
        MessageId send = stringProducerTemplate.sendAfter(supplier.get(), 60 * 5L, TimeUnit.SECONDS);
//        if (send != null) {
//            log.info("sendDelay ProducerRecord={} success,msgId={}", supplier.get(), send);
//            return;
//        }
//        log.info("sendDelay ProducerRecord={} failed", supplier.get());
    }


    public void send(Supplier<ProducerRecord<String>> supplier) throws PulsarClientException {
        MessageId send = stringProducerTemplate.send(supplier.get());
//        if (send != null) {
//            log.info("send ProducerRecord={} success,msgId={}", supplier.get(), send);
//            return;
//        }
//        log.info("send ProducerRecord={} failed", supplier.get());
    }

    public <V> void sendAsync(Supplier<ProducerRecord<V>> supplier) {
//        try {

            CompletableFuture<MessageId> future = producerTemplate.sendAsync(supplier.get());
//            if (future != null) {
//                MessageId messageId = future.get();
//                log.info("sendAsync ProducerRecord={} success,msgId={}", supplier.get(), messageId);
//                return;
//            }
//        } catch (ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        log.info("sendAsync ProducerRecord={} failed", supplier.get());
    }
}
