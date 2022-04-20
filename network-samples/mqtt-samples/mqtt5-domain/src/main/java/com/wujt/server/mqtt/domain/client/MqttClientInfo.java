package com.wujt.server.mqtt.domain.client;

/**
 * @author wujt
 */
public interface MqttClientInfo {
    /**
     * 设备侧语义约束上唯一id，标识方式会根据登录方式的不同而有所不同
     *
     * @return
     */
    String clientId();

    /**
     * 平台给与链接的唯一key，为字符串
     *
     * @return
     */
    String key();

    /**
     * 平台给与链接的唯一id
     *
     * @return
     */
    int id();

    /**
     * 接入机对每一次会话的唯一标识
     *
     * @return
     */
    String sessionKey();
}
