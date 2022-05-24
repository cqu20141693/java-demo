package com.cc.ocpp.network.cp.domian.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 充电桩类型
 * wcc 2022/4/25
 */
public enum ChargePointType {
    alternate((byte) 0x00, "交流"),
    direct((byte) 0x01, "直流"),
    alternate_direct((byte) 0x02, "交直流一体"),
    wireless((byte) 0x03, "无线"),
    alternate_wireless((byte) 0x04, "交流+无线一体"),
    bluetooth_alternate((byte) 0x10, "蓝牙+即插即用交流"),
    bluetooth_direct((byte) 0x11, "蓝牙+即插即用直流"),
    unknown((byte) -1, "未知类型"),
    ;

    private final static Map<Byte, ChargePointType> inner;

    static {
        inner = new HashMap<>();
        for (ChargePointType value : values()) {
            if (value == unknown) {
                continue;
            }
            inner.put(value.getCode(), value);
        }
    }

    public static ChargePointType parseByCode(byte code) {
        ChargePointType type = inner.get(code);
        return type == null ? unknown : type;
    }

    ChargePointType(byte code, String text) {
        this.code = code;
        this.text = text;
    }

    public byte getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    private final byte code;

    private final String text;
}
