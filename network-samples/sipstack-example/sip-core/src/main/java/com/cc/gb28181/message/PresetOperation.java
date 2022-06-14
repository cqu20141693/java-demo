package com.cc.gb28181.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@AllArgsConstructor
@Getter
public enum PresetOperation {
    //设置预置位
    SET(0x81),
    //调用预置位
    CALL(0x82),
    //删除预置位
    DEL(0x83);

    private final int code;

    public static PresetOperation of(Object operation) {
        for (PresetOperation value : values()) {
            if (value.name().equalsIgnoreCase(String.valueOf(operation)) ||
                operation.equals(value.code)
            ) {
                return value;
            }
        }
        throw new UnsupportedOperationException("不支持的预置位操作:" + operation);
    }

    public static Optional<PresetOperation> of(int code) {
        for (PresetOperation value : values()) {
            if (value.code == code) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }
}
