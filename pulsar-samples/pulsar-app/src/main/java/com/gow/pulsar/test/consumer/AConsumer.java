package com.gow.pulsar.test.consumer;

import com.gow.pulsar.core.domain.PulsarTenantUtil;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/7/7
 */
@Component
@Slf4j
public class AConsumer {

    @Autowired
    private PulsarClient pulsarClient;
    @Autowired
    private PulsarTenantUtil pulsarTenantUtil;


    private final ExecutorService executorService =
            new ThreadPoolExecutor(1, 1, Integer.MAX_VALUE, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));


    public void listen(Consumer<String> stringConsumer) {
        while (true) {
            try {

                Message<String> receive = stringConsumer.receive();
//                 next receive method no data will return null after timeout
//                Message<String> receive = consumer.receive(1000,TimeUnit.MILLISECONDS);
                log.info(
                        "AConsumer receive message.data={},msgId={},eventTime={},pubTime={},key={},value={},"
                                + "producerName={},"
                                + "properties={},sequenceId={},topic={},schemaVersion={} ",
                        new String(receive.getData()), receive.getMessageId(),
                        receive.getEventTime(), receive.getPublishTime(), receive.getKey(), receive.getValue(),
                        receive.getProducerName(), receive.getProperties(), receive.getSequenceId(),
                        receive.getTopicName(), new String(receive.getSchemaVersion()));
                stringConsumer.acknowledge(receive);
            } catch (PulsarClientException e) {
                log.info("AConsumer message error={},id={},cause={}", e.getMessage(), e.getSequenceId(), e.getCause());
            } catch (Exception e) {
                log.info("AConsumer message error={},cause={}", e.getMessage(), e.getCause());
            }
        }
    }
}
