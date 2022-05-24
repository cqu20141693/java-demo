package com.cc.netwok.gateway;

import java.util.*;

/**
 * 默认支持的传输协议
 * wcc 2022/5/1
 */
public enum DefaultTransport implements Transport {
    MQTT("MQTT"),
    UDP("UDP"),
    CoAP("CoAP"),
    TCP("TCP"),
    WebSocket("WebSocket"),
    OCPP("OCPP"),
    ;
    private final static Map<String, DefaultTransport> inner;

    static {
        inner = new HashMap<>();
        for (DefaultTransport value : values()) {
            inner.put(value.getId(), value);
        }
    }

    DefaultTransport(String id) {
        this.id = id;
    }

    private String id;

    public static List<Transport> get() {
        return new ArrayList<>(inner.values());
    }

    public static Optional<Transport> lookup(String id) {
        return Optional.ofNullable(inner.get(id.toUpperCase()));
    }

    @Override
    public String getId() {
        return id;
    }
}
