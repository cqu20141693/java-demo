package com.cc.ocpp.network.cp.domian.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * wcc 2022/4/30
 */
public enum ProductType {

    alternate_private1("01", "交流私桩1型"),
    alternate_busy1("02", "交流运营1型"),
    direct_1("03", "直流桩1型"),
    three_phase_ac1("04", "三相交流1型"),
            ;
    private final static Map<String, ProductType> inner;

    static {
        inner = new HashMap<>();
        for (ProductType value : values()) {
            inner.put(value.getCode(), value);
        }
    }

    public static ProductType parseByCode(String code) {
        return inner.get(code);
    }

    ProductType(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    private final String code;
    private String text;
}
