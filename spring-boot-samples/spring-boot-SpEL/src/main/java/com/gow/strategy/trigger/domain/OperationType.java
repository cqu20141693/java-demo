package com.gow.strategy.trigger.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gow
 * @date 2021/9/2
 */
public enum OperationType {
    EQ("EQ", "=="),
    NE("NE", "!="),
    LT("LT", "<"),
    LTE("LTE", "<="),
    GT("GT", ">"),
    GTE("GTE", ">="),
    UNKNOWN("UNKNOWN", "");

    private static final Map<String, OperationType> INNER = new HashMap<>();
    private String type;
    private String code;

    OperationType(String type, String code) {
        this.type = type;
        this.code = code;
    }

    public static OperationType parseFromType(String type) {
        return type == null ? UNKNOWN : INNER.getOrDefault(type.toLowerCase(), UNKNOWN);
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return this.code;
    }

    static {
        OperationType[] types = values();

        for (OperationType t : types) {
            INNER.put(t.type, t);
        }

    }
}
