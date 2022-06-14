package com.cc.gb28181.media.enums;

/**
 * wcc 2022/5/25
 */
public enum ChannelFeature {
    localRecord("localRecord", "支持本地录像");

    ChannelFeature(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    private final String value;
    private final String text;
}
