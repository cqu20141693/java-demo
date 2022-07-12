package com.cc.netwok.message;

/**
 * 消息类型
 * wcc 2022/6/29
 */
public enum MessagePayloadType {
    JSON, STRING, BINARY, HEX, UNKNOWN;

    public static MessagePayloadType of(String of) {
        for (MessagePayloadType value : MessagePayloadType.values()) {
            if (value.name().equalsIgnoreCase(of)) {
                return value;
            }
        }
        return UNKNOWN;
    }
}
