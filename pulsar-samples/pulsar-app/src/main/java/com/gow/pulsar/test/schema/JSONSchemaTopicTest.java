package com.gow.pulsar.test.schema;

import com.gow.pulsar.listener.IMessageListener;
import com.gow.pulsar.producer.CommonProducer;
import com.gow.pulsar.producer.PulsarProducer;
import com.gow.pulsar.core.domain.PulsarProperties;
import com.gow.pulsar.test.config.PulsarTestConfig;
import com.gow.pulsar.test.schema.model.RawData;
import com.gow.pulsar.test.schema.model.RawDataV2;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.SubscriptionType;
import org.apache.pulsar.shade.org.apache.commons.lang3.RandomStringUtils;
import org.apache.pulsar.shade.org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/7/19
 */
@Component("jsonSchemaTopic")
@Slf4j
public class JSONSchemaTopicTest implements CommandLineRunner {

    @Autowired
    private PulsarClient pulsarClient;
    @Autowired
    private PulsarProperties properties;

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private PulsarTestConfig pulsarTestConfig;
    // failOver
    private final String topic = "persistent://gow/persistent/json-schema-test-topic";

    @Override
    public void run(String... args) throws Exception {

        if (pulsarTestConfig.getJsonSchemaTopic()) {
            originSchema();
            v2Schema();
        } else {
            log.info("JSONSchemaTopicTest not open");
        }
    }

    private void v2Schema() throws PulsarClientException {
        Producer<RawDataV2> stringProducer =
                pulsarClient.newProducer(Schema.JSON(RawDataV2.class)).topic(topic)
                        .create();
        PulsarProducer<RawDataV2> pulsarProducer = new CommonProducer<>(stringProducer, taskScheduler);

        Supplier<String> keySupplier = () -> LocalDateTime.now().toString();
        Supplier<RawDataV2> valueSupplier =
                () -> {
                    RawDataV2 rawData = new RawDataV2();
                    rawData.setTopic(RandomStringUtils.randomAlphanumeric(8));
                    rawData.setType(RandomUtils.nextInt());
                    rawData.setData(RandomStringUtils.randomAlphanumeric(8).getBytes(
                            StandardCharsets.UTF_8));
                    rawData.setVersion("v2");
                    return rawData;
                };
        pulsarProducer.scheduleSend(1000L, 5000L, keySupplier, valueSupplier);
        log.info("JSONSchemaTopicTest v2 listen invoked ");

        try {
            Consumer<RawDataV2> subscribe = pulsarClient.newConsumer(Schema.JSON(RawDataV2.class)).topic(topic)
                    .subscriptionType(SubscriptionType.Shared).subscriptionName("json-schema-shared-consumer")
                    .messageListener(new IMessageListener<>())
                    .subscribe();
        } catch (PulsarClientException e) {
            e.printStackTrace();
        }
    }

    private void originSchema() throws PulsarClientException {
        Producer<RawData> stringProducer =
                pulsarClient.newProducer(Schema.JSON(RawData.class)).topic(topic)
                        .create();
        PulsarProducer<RawData> pulsarProducer = new CommonProducer<>(stringProducer, taskScheduler);

        Supplier<String> keySupplier = () -> LocalDateTime.now().toString();
        Supplier<RawData> valueSupplier =
                () -> {
                    RawData rawData = new RawData();
                    rawData.setTopic(RandomStringUtils.randomAlphanumeric(8));
                    rawData.setType(RandomUtils.nextInt());
                    rawData.setData(RandomStringUtils.randomAlphanumeric(8).getBytes(
                            StandardCharsets.UTF_8));
                    return rawData;
                };
      //  pulsarProducer.scheduleSend(1000L, 5000L, keySupplier, valueSupplier);
        IntStream.range(0, 3).forEach(index -> {
            log.info("JSONSchemaTopicTest listen invoke index={}", index);
//            executorService.submit(() -> listen(index));
            try {
                Consumer<RawData> subscribe = pulsarClient.newConsumer(Schema.JSON(RawData.class)).topic(topic)
                        .subscriptionType(SubscriptionType.Shared).subscriptionName("json-schema-shared-consumer")
                        .messageListener(new IMessageListener<>())
                        .subscribe();
            } catch (PulsarClientException e) {
                e.printStackTrace();
            }
        });
    }
}
