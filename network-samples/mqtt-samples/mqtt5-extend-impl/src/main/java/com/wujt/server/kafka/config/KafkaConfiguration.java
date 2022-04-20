package com.wujt.server.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;
import java.util.Properties;

/**
 * kafka服务器配置说明
 *
 * @author wujt
 */
@Configuration
@ConfigurationProperties(prefix = "extend.kafka")
@RefreshScope
public class KafkaConfiguration {

    /**
     * the servers of kafka  localhost:9092
     */
    @NotNull
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

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public Integer getLingerMS() {
        return lingerMS;
    }

    public void setLingerMS(Integer lingerMS) {
        this.lingerMS = lingerMS;
    }

    public Integer getMaxRequestSize() {
        return maxRequestSize;
    }

    public void setMaxRequestSize(Integer maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
    }

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

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

    public Properties consumerProps() {
        Properties props = new Properties();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.servers);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 100);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 15000);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }
}
