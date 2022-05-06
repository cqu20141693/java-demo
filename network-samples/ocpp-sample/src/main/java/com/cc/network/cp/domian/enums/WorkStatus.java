package com.cc.network.cp.domian.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 有线枪工作状态
 * wcc 2022/5/5
 */
public enum WorkStatus {
    idle((byte) 0x00, "空闲中"),
    electronic_lock_open((byte) 0x01, "电子锁打开"),
    gun_removed((byte) 0x02, "枪头已拔下"),
    gun_attached_car((byte) 0x03, "枪已连接车"),
    charging_waiting((byte) 0x04, "充电等待中"),
    charging((byte) 0x05, "充电中"),
    charged((byte) 0x06, "充电结束"),
    gun_disconnect_car((byte) 0x07, "枪断开与车连接"),
    scheduling((byte) 0x08, "定时中"),
    reservation((byte) 0x09, "预约中"),
    unknown((byte) 0xff, "预留"),

    ;
    private final static Map<Byte, WorkStatus> inner;

    static {
        inner = new HashMap<>();
        for (WorkStatus value : values()) {
            if (value == unknown) {
                continue;
            }
            inner.put(value.code, value);
        }
    }

    WorkStatus(byte code, String text) {
        this.code = code;
        this.text = text;
    }

    public static WorkStatus parseByCode(byte code) {
        WorkStatus status = inner.get(code);
        return status == null ? unknown : status;
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
