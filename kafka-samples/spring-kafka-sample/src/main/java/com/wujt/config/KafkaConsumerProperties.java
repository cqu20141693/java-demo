package com.wujt.config;

import lombok.Data;
import org.apache.kafka.common.IsolationLevel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wujt  2021/4/29
 */
@Data
@Configuration
@ConfigurationProperties("kafka.consumer")
public class KafkaConsumerProperties {
    private String bootstrapServers;
    private String groupId;
    private Class<?> keyDeserializer;
    private Class<?> valueDeserializer;
    private String clientId;
    /**
     * 是否自动提交offset，true会存在数据丢失
     */
    private Boolean enableAutoCommit;
    private Integer autoCommitInterval;
    /**
     * earliest,latest
     * none 分区不存在已提交的offset，则抛出异常
     */
    private String autoOffsetReset;
    /**
     * fetch 消息
     */
    private Integer fetchMaxWait;
    private Integer fetchMinSize;
    private Integer maxPollRecords;

    private Integer heartbeatInterval;
    private Integer maxPollIntervalMs;
    private IsolationLevel isolationLevel;

    /**
     * 分区分配策略： range,roundrobin,sticky
     */
    private String partitionAssignStrategy;

}
