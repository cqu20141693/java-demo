package com.cc.network.cp.domian.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * wcc 2022/4/25
 */
public enum NetworkType {
    fourG((byte) 0, "4G"),
    wifi((byte) 1, "wifi"),
    wired((byte) 2, "有线"),
    nb_iot((byte) 2, "NB-IOT"),
    unknown((byte) -1, "unknown"),
    ;

    private final static Map<Byte, NetworkType> inner;

    static {
        inner = new HashMap<>();
        for (NetworkType value : values()) {
            if (unknown == value) {
                continue;
            }
            inner.put(value.code, value);
        }
    }

    NetworkType(byte code, String text) {
        this.code = code;
        this.text = text;
    }

    public static NetworkType parseByCode(byte code) {
        NetworkType type = inner.get(code);
        return type == null ? unknown : type;
    }

    public byte getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    private byte code;
    private String text;
}
