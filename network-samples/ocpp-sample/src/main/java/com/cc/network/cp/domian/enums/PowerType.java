package com.cc.network.cp.domian.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 功率类型
 * wcc 2022/4/30
 */
public enum PowerType {

    unknown((byte) 0x00, "功率未知"),
    single17((byte) 0x01, "单项1.7kw（220V/8A）"),
    single35((byte) 0x02, "单项3.5kw（220V/16A）"),
    single7((byte) 0x03, "单项7kw（220V/32A）"),
    three11((byte) 0x10, "三项 11kw（380V/16A）"),
    three22((byte) 0x11, "三项 22kw (380V/32A)"),
    direct15((byte) 0x20, "直流 15kw"),
    direct30((byte) 0x21, "直流 30kw"),
    direct45((byte) 0x22, "直流 45kw"),
    direct60((byte) 0x23, "直流 60kw"),
    direct90((byte) 0x24, "直流 90kw"),
    direct120((byte) 0x25, "直流 120kw"),
    direct180((byte) 0x26, "直流 180kw"),
    direct240((byte) 0x27, "直流 240kw"),
    direct300((byte) 0x28, "直流 300kw"),
    direct360((byte) 0x29, "直流 360kw"),
    ;

    private final static Map<Byte, PowerType> inner;

    static {
        inner = new HashMap<>();
        for (PowerType value : values()) {
            inner.put(value.getCode(), value);
        }
    }

    public static PowerType parseByCode(byte code) {
        return inner.get(code);
    }

    PowerType(Byte code, String text) {
        this.code = code;
        this.text = text;
    }

    public Byte getCode() {
        return code;
    }

    private final Byte code;
    private String text;
}
