package com.gow.spi.impl;

import com.alibaba.fastjson.JSONObject;
import com.gow.spi.config.KafkaConfig;
import com.wujt.spi.MessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * @author wujt
 */
@Slf4j
public class KafkaMsgProducer implements MessageProducer {

    private KafkaProducer<String, String> producer;

    private KafkaConfig kafkaConfig;

    public KafkaMsgProducer() {
        // producer = (KafkaProducer<String, String>) SpringContextUtil.getBean(KafkaProducer.class);
        producer = null;
//        kafkaConfig = (KafkaConfig) SpringContextUtil.getBean("kafkaConfig");
        kafkaConfig = null;
    }

    @Override
    public <T> Boolean send(T t) {

        log.info("kafka producer {} send message={} ", producer, t);
       // ProducerRecord<String, String> record = new ProducerRecord<>(kafkaConfig.getTopic(), JSONObject.toJSONString(t));
        //return producer.send(record).isDone();
        return true;
    }
}
