package com.wujt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wujt
 */
@SpringBootApplication
@Slf4j
public class KafkaConsumerApp implements CommandLineRunner {

    private AtomicInteger originTotal = new AtomicInteger(0);
    private AtomicInteger checkedTotal = new AtomicInteger(0);

    public static void main(String[] args) {
        SpringApplication.run(KafkaConsumerApp.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("server is start");
    }

//    @KafkaListener(topics = "origin-data-topic", groupId = "stress-group", containerFactory = "kafkaListenerContainerFactory")
//    public void originData(List<String> result, Acknowledgment ack) {
//        result.forEach(data -> log.info("origin total={}", originTotal.incrementAndGet())
//        );
//    }
//
//    @KafkaListener(topics = "cmd-topic", groupId = "stress-group", containerFactory = "kafkaListenerContainerFactory")
//    public void cmdData(List<String> result) {
//        result.forEach(data -> log.info("cmd-topic,data={}", data));
//    }
//
//    @KafkaListener(topics = "cmd-topic-checked", groupId = "stress-group", containerFactory = "kafkaListenerContainerFactory")
//    public void cmdDataChecked(List<String> result) {
//        result.forEach(data -> log.info("cmd-topic-checked,data={}", data));
//    }
//    @KafkaListener(topics = "chan-conn-topic", groupId = "stress-group", containerFactory = "kafkaListenerContainerFactory")
//    public void chanConnTopic(List<String> result) {
//        result.forEach(data -> log.info("chan-conn-topic,data={}", data));
//    }
//    @KafkaListener(topics = "data-topic-checked", groupId = "stress-group", containerFactory = "dataContainerFactory")
//    public void checkedData(List<String> result) { chan-conn-topic
//        result.forEach(data -> log.info("checked total={}", checkedTotal.incrementAndGet())
//        );
//    }
}
