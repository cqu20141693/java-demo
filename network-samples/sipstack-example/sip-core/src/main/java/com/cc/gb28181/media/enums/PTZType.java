package com.cc.gb28181.media.enums;

/**
 * PTZ 类型
 * wcc 2022/5/25
 */
public enum PTZType {
    unknown(0, "未知"),
    ball(1, "球机"),
    hemisphere(2, "半球机"),
    fixed(3, "固定抢机"),
    remoteControl(4, "遥控抢机");

    PTZType(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    public Integer getValue() {
        return value;
    }

    private final Integer value;
    private final String text;
}
