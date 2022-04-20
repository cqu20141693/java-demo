package com.gow.codec.model.topology;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gow
 * @date 2021/9/23
 */
public enum TopologyOperationType {
    BIND("绑定", (byte) 1),
    UNBIND("解绑", (byte) 2);

    private final String operationName;

    private final Byte index;

    public Byte getIndex() {
        return index;
    }

    TopologyOperationType(String operationName, Byte index) {
        this.operationName = operationName;
        this.index = index;
    }

    private static final Map<Byte, TopologyOperationType> INNER;

    static {
        INNER = new HashMap<>();
        for (TopologyOperationType value : TopologyOperationType.values()) {
            INNER.put(value.getIndex(), value);
        }
    }

    public static TopologyOperationType parseFromIndex(Byte index) {
        return INNER.get(index);
    }
}
