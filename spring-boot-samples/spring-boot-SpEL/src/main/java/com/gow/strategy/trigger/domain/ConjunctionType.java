package com.gow.strategy.trigger.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gow
 * @date 2021/9/2
 */
public enum ConjunctionType {
    AND("AND"),
    OR("OR"),
    UNKNOWN("NULL");

    private static final Map<String, ConjunctionType> INNER = new HashMap();
    private String code;

    ConjunctionType(String code) {
        this.code = code;
    }

    public static ConjunctionType parseFromCode(String code) {
        return code == null ? UNKNOWN : INNER.getOrDefault(code.toLowerCase(), UNKNOWN);
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    static {
        ConjunctionType[] types = values();

        for (ConjunctionType t : types) {
            INNER.put(t.code, t);
        }
    }
}
