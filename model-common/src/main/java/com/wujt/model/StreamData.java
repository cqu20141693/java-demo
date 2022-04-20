package com.wujt.model;

import com.wujt.enums.MqttQoS;
import lombok.Data;

import java.util.Map;

/**
 * @author wujt
 */
@Data
public class StreamData {

    private Boolean gateway;
    private String dynamicSecret;
    private String sessionKey;
    private Map<String, String> extend;
    private String deviceKey;
    private String topic;
    private byte[] payload;
    private long time;
    private MqttQoS mqttQoS;
    private Boolean retain;
    private Boolean dup;

    // 测试事件
    private int event;
}
