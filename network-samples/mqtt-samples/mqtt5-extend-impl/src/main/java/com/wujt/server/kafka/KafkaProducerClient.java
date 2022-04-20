package com.wujt.server.kafka;


import com.wujt.server.executor.ProcessExecutorGroup;
import com.wujt.server.kafka.config.KafkaConfiguration;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;

/**
 * kafka client的描述
 *
 *
 * @date 2018/4/1
 */
@Component
public class KafkaProducerClient {

    @Autowired
    private KafkaConfiguration kafkaConfiguration;

    @Autowired
    private ProcessExecutorGroup processExecutorGroup;

    private KafkaProducer<String, String> producer;

    @PreDestroy
    public void close() {
        if (producer != null) {
            producer.close();
            producer = null;
        }
    }

    @PostConstruct
    public void init() {
        if (kafkaConfiguration.isEnable()) {
            producer = new KafkaProducer<>(kafkaConfiguration.producerProps());
        }
    }

    CompletableFuture<RecordMetadata> send(ProducerRecord<String, String> record, boolean needAsync) {
        if (producer != null) {
            if (needAsync) {
                return CompletableFuture.supplyAsync(() -> {
                    try {
                        return producer.send(record).get(1500, TimeUnit.MILLISECONDS);
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }
                }, processExecutorGroup.getExecutorFromExtend(record.key()));
            } else {
                //不进行异步监听，直接发
                producer.send(record);
            }
        }
        return null;
    }
}
