package com.wujt.config;

import lombok.Data;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wujt  2021/4/28
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "kafka.producer")
public class KafkaProducerProperties {

    /**
     * broker address
     */
    private String bootstrapServers;

    /**
     * producer id
     */
    private String clientId;

    /**
     * key 序列化
     */
    private Class<?> keySerializer = StringSerializer.class;
    /**
     * value 序列化
     */
    private Class<?> valueSerializer = StringSerializer.class;

    // 保证数据不丢失
    /**
     * ack mode
     */
    private String acks;
    /**
     * 一次请求超时时间
     */
    private Integer requestTimeoutMs;
    /**
     * 重试次数
     */
    private Integer retries;
    /**
     * 重试间隔时长
     */
    private Integer retryInterval;


    // 保证数据有序性
    /**
     * 一个连接处理的最大请求数
     */
    private Integer maxRequestPerConnection;

    // 提高吞吐量和性能
    /**
     * batch   byte size
     */
    private Integer batchSize;
    /**
     * batch 发送延迟时间
     */
    private Integer lingerMs;

    /**
     * 压缩类型
     */
    private String compressionType;

    // 配置合理的缓冲区
    /**
     * 缓存区大小 byte size
     */
    private long bufferMemory;
    /**
     * 缓存区不足最长阻塞时长后抛出异常
     */
    private Integer maxBlockMs;

}
