package com.cc.gb28181.media.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * wcc 2022/5/24
 */
public enum ChannelStatus {
    online("ON", "在线"),

    lost("VLOST", "视频丢失"),
    defect("DEFECT", "故障"),
    add("ADD", "新增"),
    delete("DEL", "删除"),
    update("UPDATE", "更新"),
    offline("OFF", "离线"),
    ;

    private final static Map<String, ChannelStatus> inner;

    static {
        inner = new HashMap<>();
        for (ChannelStatus value : values()) {
            inner.put(value.getCode(), value);
        }
    }

    public static ChannelStatus of(String event) {
        return inner.get(event);
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    private final String code;

    private final String text;

    ChannelStatus(String code, String text) {
        this.code = code;
        this.text = text;
    }

}
