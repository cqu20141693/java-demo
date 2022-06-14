package com.cc.things.codec.defaults;

import com.cc.things.codec.Codec;
import com.cc.things.codec.Payload;

import javax.annotation.Nonnull;

/**
 * bool 类型编解码器
 * wcc 2022/6/4
 */
public class BooleanCodec implements Codec<Boolean> {

    public static BooleanCodec INSTANCE = new BooleanCodec();

    private BooleanCodec() {

    }

    @Override
    public Class<Boolean> forType() {
        return Boolean.class;
    }

    @Override
    public Boolean decode(@Nonnull Payload payload) {
        byte[] data = payload.getBytes(false);

        return data.length > 0 && data[0] > 0;
    }

    @Override
    public Payload encode(Boolean body) {
        return Payload.of(new byte[]{body ? (byte) 1 : 0});
    }

}
