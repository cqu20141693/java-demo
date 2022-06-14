package com.cc.gb28181.media.enums;

/**
 * wcc 2022/5/25
 */
public enum GatewayStatus {


    enabled("enabled", "启用"),
    disabled("disabled", "禁用");

    private final String text;

    GatewayStatus(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private final String value;

}
