package com.cc.things.codec.defaults;

import com.cc.things.codec.Codec;
import com.cc.things.codec.Payload;
import com.gow.codec.bytes.BytesUtils;

import javax.annotation.Nonnull;

/**
 * double 类型编解码器
 * wcc 2022/6/4
 */
public class DoubleCodec implements Codec<Double> {

    public static DoubleCodec INSTANCE = new DoubleCodec();

    private DoubleCodec() {

    }

    @Override
    public Class<Double> forType() {
        return Double.class;
    }

    @Override
    public Double decode(@Nonnull Payload payload) {
        byte[] bytes = payload.getBytes(false);
        return BytesUtils.beToDouble(bytes, 0, bytes.length);
    }

    @Override
    public Payload encode(Double body) {
        return Payload.of(BytesUtils.doubleToBe(body));
    }


}
