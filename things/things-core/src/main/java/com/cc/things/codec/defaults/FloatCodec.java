package com.cc.things.codec.defaults;

import com.cc.things.codec.Codec;
import com.cc.things.codec.Payload;
import com.gow.codec.bytes.BytesUtils;

import javax.annotation.Nonnull;

/**
 * float 类型编解码器
 * wcc 2022/6/4
 */
public class FloatCodec implements Codec<Float> {

    public static FloatCodec INSTANCE = new FloatCodec();

    private FloatCodec() {

    }

    @Override
    public Class<Float> forType() {
        return Float.class;
    }

    @Override
    public Float decode(@Nonnull Payload payload) {
        byte[] bytes = payload.getBytes(false);
        return BytesUtils.beToFloat(bytes, 0, bytes.length);
    }

    @Override
    public Payload encode(Float body) {
        return Payload.of(BytesUtils.floatToBe(body));
    }


}
