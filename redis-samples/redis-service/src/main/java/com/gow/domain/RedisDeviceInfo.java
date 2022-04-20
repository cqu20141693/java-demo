package com.gow.domain;

/**
 * @author wujt
 */
public class RedisDeviceInfo {
    /**
     * session string
     */
    private String session;
    /**
     * broker ip
     */
    private String ip;
    /**
     * port
     */
    private int port;
    /**
     * 加密时的动态密钥
     */
    private String dynamicSecret;
    /**
     * 网关链路标识
     */
    private Boolean gateway;

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDynamicSecret() {
        return dynamicSecret;
    }

    public void setDynamicSecret(String dynamicSecret) {
        this.dynamicSecret = dynamicSecret;
    }

    public Boolean getGateway() {
        return gateway;
    }

    public void setGateway(Boolean gateway) {
        this.gateway = gateway;
    }

    public String toRedisValue() {
        return ip + ":" + port + ":" + session + ":" + gateway + (dynamicSecret != null ? ":" + dynamicSecret : "");
    }

    public RedisDeviceInfo fromRedisValue(String value) {
        String[] data = value.split(":");
        ip = data[0];
        port = Integer.parseInt(data[1]);
        session = data[2];
        //gateway = Boolean.parseBoolean(data[3]);
        gateway = Boolean.valueOf(data[3]);
        if (data.length >= 5) {
            dynamicSecret = data[4];
        }
        return this;
    }


    @Override
    public String toString() {
        return "RedisDeviceInfo{" +
                "session='" + session + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port + '\'' +
                ", dynamicSecret" + '\'' +
                ", gateway" +
                '}';
    }
}
