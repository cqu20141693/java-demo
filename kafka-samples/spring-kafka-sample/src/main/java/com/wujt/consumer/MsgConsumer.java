package com.wujt.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wujt  2021/4/29
 */
@Component
@Slf4j
public class MsgConsumer {

    // @KafkaListener(topics = "${kafka.topic.data-topic}", groupId = "error-retry-group", containerFactory = "manualBatchContainerFactory")
    public void originData(List<String> result) {
        log.info("data:{}", result);
        throw new RuntimeException("test-exception");
    }

    @KafkaListener(topics = "${kafka.topic.data-topic}", groupId = "error-retry-group", containerFactory = "manualSingleContainerFactory")
    public void single(String result) {
        log.info("data:{}", result);
        throw new RuntimeException("test-exception");
    }


    @KafkaListener(topics = "${kafka.topic.data-topic}" + ".DLT", groupId = "error-retry-group", containerFactory = "manualSingleContainerFactory")
    public void dltData(String result) {
        log.info("dltData:{}", result);
    }

    // @KafkaListener(topics = "${kafka.topic.data-topic}", groupId = "error-retry-group", containerFactory = "manualKafkaListenerContainerFactory")
    public void originData(List<String> result, Acknowledgment acknowledgment) {
        log.info("data:{}", result);
        throw new RuntimeException("test-exception");
    }
}
