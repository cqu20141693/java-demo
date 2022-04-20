package com.wujt.distributeTask.demo;


/**
 * mqtt 数据接入时的加密
 *
 */
public class DeviceWelcomeInfo {
    private String deviceKey;
    private String uploadToken;
    private String queryToken;
    private String cmdToken;
    /**
     * 设备的静态秘钥，设备创建时生成的秘钥
     */
    private String deviceSecret;
    /**
     * 设备的动态秘钥，只对此链接有效
     */
    private String dynamicSecret;

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    public String getUploadToken() {
        return uploadToken;
    }

    public void setUploadToken(String uploadToken) {
        this.uploadToken = uploadToken;
    }

    public String getQueryToken() {
        return queryToken;
    }

    public void setQueryToken(String queryToken) {
        this.queryToken = queryToken;
    }

    public String getCmdToken() {
        return cmdToken;
    }

    public void setCmdToken(String cmdToken) {
        this.cmdToken = cmdToken;
    }

    public String getDeviceSecret() {
        return deviceSecret;
    }

    public void setDeviceSecret(String deviceSecret) {
        this.deviceSecret = deviceSecret;
    }

    public String getDynamicSecret() {
        return dynamicSecret;
    }

    public void setDynamicSecret(String dynamicSecret) {
        this.dynamicSecret = dynamicSecret;
    }
}
