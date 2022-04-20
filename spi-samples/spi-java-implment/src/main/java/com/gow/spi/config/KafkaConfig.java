package com.gow.spi.config;

import lombok.Data;
import lombok.ToString;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author wujt
 */
@Configuration
@ConfigurationProperties(prefix = "extend.kafka")
@Data
@ToString
public class KafkaConfig {
    /**
     * the servers of kafka  localhost:9092
     */
//    @NotNull
    private String servers;
    /**
     * the group of kafka,default value: mqtt-broker-device-group
     */
    private String group = "mqtt-broker-device-group";
    /**
     * enable data output to kafka,default value: true
     */
    private boolean enable = true;
    // 生产者一个批次等待的大小，当延迟时间内等待的消息字节数在配置内可以一个批次发送
    private Integer batchSize = 16384;
    //消息延迟发送的毫秒数，目的是为了等待多个消息，在同一批次发送，减少网络请求
    private Integer lingerMS = 1;
    // 生产者一个批次发送的最大字节数
    private Integer maxRequestSize = 1048576;
    //topic
    private String topic;

    public Properties producerProps() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.servers);
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        props.put(ProducerConfig.LINGER_MS_CONFIG, lingerMS);
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, maxRequestSize);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

}
