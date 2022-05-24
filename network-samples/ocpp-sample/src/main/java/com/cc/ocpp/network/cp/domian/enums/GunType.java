package com.cc.ocpp.network.cp.domian.enums;

import com.cc.ocpp.network.cp.domian.heart.DirectField;
import com.cc.ocpp.network.cp.domian.heart.SingleACField;
import com.cc.ocpp.network.cp.domian.heart.WirelessField;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 枪类型
 * wcc 2022/5/5
 */
public enum GunType {
    single_phase_ac((byte) 0x00, "单相交流", SingleACField::parse),
    three_phase_ac((byte) 0x01, "三相交流", DirectField::parse),
    direct((byte) 0x02, "直流", DirectField::parse),
    wireless((byte) 0x03, "无线", WirelessField::parse),
    ;
    private final static Map<Byte, GunType> inner;

    static {
        inner = new HashMap<>();
        for (GunType value : values()) {
            inner.put(value.code, value);
        }
    }

    GunType(byte code, String text, Function<byte[], Object> converter) {
        this.code = code;
        this.text = text;
        this.fieldConverter = converter;
    }

    public static GunType parseByCode(byte code) {
        return inner.get(code);
    }

    public byte getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    private final byte code;
    private final String text;
    private final Function<byte[], Object> fieldConverter;
}
