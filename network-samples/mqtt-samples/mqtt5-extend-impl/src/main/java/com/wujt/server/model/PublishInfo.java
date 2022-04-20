package com.wujt.server.model;

import com.wujt.server.mqtt.domain.client.MqttDeviceClientInfo;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.Data;

/**
 * report messages to the platform
 *
 * @author wujt
 */
@Data
public class PublishInfo extends MqttDeviceClientInfo {
    /**
     * 推送报文topic
     */
    private String topic;
    /**
     * 推送报文payload
     */
    private byte[] payload;

    /**
     * 时间戳
     */
    private long time;
    // mqtt 服务质量，路由始终
    private MqttQoS mqttQoS;
    // 保留标识
    private Boolean retain;
    // 重复标识
    private Boolean dup;
}
