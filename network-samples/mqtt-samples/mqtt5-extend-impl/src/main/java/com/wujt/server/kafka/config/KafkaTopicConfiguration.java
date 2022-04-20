package com.wujt.server.kafka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

/**
 * kafka topic 配置说明
 *
 * @author wujt
 */
@Configuration
@ConfigurationProperties(prefix = "extend.kafka.topic")
public class KafkaTopicConfiguration {

    /**
     * 原生publish数据
     */
    @NotNull
    private String originData = "origin-data-topic";
    /**
     * channel 连接事件数据
     */
    @NotNull
    private String channelConnEvent = "chan-conn-topic";

    /**
     * 用于对qos 消息send,ack,expired 事件的通知
     */
    @NotNull
    private String qosEvent = "qos-event-topic";

    public String getOriginData() {
        return originData;
    }

    public String getChannelConnEvent() {
        return channelConnEvent;
    }

    public String getQosEvent() {
        return qosEvent;
    }

}
