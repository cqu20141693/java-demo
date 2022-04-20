package com.wujt.server.mqtt.domain.client;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

/**
 * 以设备身份连接mqtt服务器鉴权通过后, 鉴权服务器返回的数据
 * <p>
 * 当首次鉴权通过时，会返回设备侧鉴权的三个Token；第二次返回时，就不会返回
 * <p>
 * 鉴权返回给设备时，第一次会返回：deviceKey、三个Token、deviceSecret
 * 后续只会返回deviceKey
 *
 * @author  wujt
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class MqttDeviceClientInfo implements MqttClientInfo {
    private int userId;
    private String userKey;
    private int productId;
    private String productKey;
    private int deviceId;
    private String deviceKey;

    // 网关
    private Boolean gateway;
    // 动态密码
    private String dynamicSecret;
    // 会话唯一标识： 由接入机生成透传
    private String sessionKey;


    // 用于后期扩展透传
    private Map<String, String> extend;
    // clientId
    private String clientId;

    @Override
    public String clientId() {
        // clientId 为平台唯一标识deviceKey, 不参与上行透传
        return this.deviceKey;
    }

    @Override
    public String key() {
        return this.deviceKey;
    }

    @Override
    public int id() {
        return this.deviceId;
    }

    @Override
    public String sessionKey() {
        return sessionKey;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    public String getClientId() {
        return clientId;
    }

    public Boolean getGateway() {
        return gateway;
    }

    public void setGateway(Boolean gateway) {
        this.gateway = gateway;
    }

    public Map<String, String> getExtend() {
        return extend;
    }

    public void setExtend(Map<String, String> extend) {
        this.extend = extend;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getDynamicSecret() {
        return dynamicSecret;
    }

    public void setDynamicSecret(String dynamicSecret) {
        this.dynamicSecret = dynamicSecret;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }
}
