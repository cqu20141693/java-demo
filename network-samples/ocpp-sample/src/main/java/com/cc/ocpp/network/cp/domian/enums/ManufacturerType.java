package com.cc.ocpp.network.cp.domian.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 厂商类型 :2byte ASCII码,“Z1”（挚达），“W1”（沃尔沃），“L1”（领克）
 * wcc 2022/4/25
 */
public enum ManufacturerType {
    qing_da("Z1", "擎达"),
    volvo("W1", "沃尔沃"),
    ling_co("Z1", "领克"),
    ;
    private final static Map<String, ManufacturerType> inner;

    static {
        inner = new HashMap<>();
        for (ManufacturerType value : values()) {
            inner.put(value.getCode(), value);
        }
    }

    public static ManufacturerType parseByCode(String code) {
        return inner.get(code);
    }

    ManufacturerType(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    private final String code;
    private String text;
}
