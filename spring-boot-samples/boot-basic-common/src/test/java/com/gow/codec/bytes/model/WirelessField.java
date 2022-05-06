package com.gow.codec.bytes.model;

import lombok.Data;

/**
 * 无线数据域
 * wcc 2022/5/5
 */
@Data
public class WirelessField {

    public static WirelessField parse(byte[] bytes) {
        return new WirelessField();
    }
}
