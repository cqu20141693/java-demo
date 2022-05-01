package com.cc.network;

/**
 * wcc 2022/4/26
 */
public enum DefaultNetworkType implements NetworkType {
    MQTT_SERVER("MQTT服务"),
    OCPP_QD_SERVER("充电桩服务端"),
    ;

    DefaultNetworkType(String name) {
        this.name = name;
    }

    private final String name;


    @Override
    public String getId() {
        return name();
    }

    @Override
    public String getName() {
        return name;
    }
}
