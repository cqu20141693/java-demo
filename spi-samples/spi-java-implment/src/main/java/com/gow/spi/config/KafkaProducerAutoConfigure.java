package com.gow.spi.config;

import com.alibaba.fastjson.JSONObject;
import com.gow.spring.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wujt
 */
@Configuration
@Slf4j
@ConditionalOnMissingBean(KafkaProducer.class)
@ConditionalOnProperty(prefix = "producer", value = "name", havingValue = "kafka")
@EnableConfigurationProperties({KafkaConfig.class})
public class KafkaProducerAutoConfigure {

    @Bean
    public KafkaProducer kafkaProducer(KafkaConfig kafkaConfig, ApplicationContext applicationContext) {
        SpringContextUtil.setApplicationContext(applicationContext);
        log.info("properties={}", JSONObject.toJSONString(kafkaConfig.producerProps()));
        return new KafkaProducer<>(kafkaConfig.producerProps());
    }
}
