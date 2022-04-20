package com.gow.strategy.trigger.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gow
 * @date 2021/9/2
 */
public enum TriggerStatus {
    NOT_ACTIVE(-1),
    INIT(0),
    ACTIVE(1);
    private static final Map<Integer, TriggerStatus> INNER = new HashMap<>();
    private Integer code;

    private TriggerStatus(Integer code) {
        this.code = code;
    }

    public static TriggerStatus parseFromCode(Integer code) {
        return INNER.get(code);
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    static {
        TriggerStatus[] statuses = values();

        for (TriggerStatus t : statuses) {
            INNER.put(t.code, t);
        }

    }
}
