package com.cc.things.codec.defaults;

import com.cc.things.codec.Codec;
import com.cc.things.codec.Payload;
import com.gow.codec.bytes.BytesUtils;

import javax.annotation.Nonnull;

/**
 * int 类型编解码器
 * wcc 2022/6/4
 */
public class IntegerCodec implements Codec<Integer> {

    public static IntegerCodec INSTANCE = new IntegerCodec();

    private IntegerCodec() {

    }

    @Override
    public Class<Integer> forType() {
        return Integer.class;
    }

    @Override
    public Integer decode(@Nonnull Payload payload) {
        return BytesUtils.beToInt(payload.getBytes(false));
    }

    @Override
    public Payload encode(Integer body) {
        return Payload.of(BytesUtils.intToBe(body));
    }


}
