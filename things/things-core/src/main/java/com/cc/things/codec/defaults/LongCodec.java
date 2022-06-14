package com.cc.things.codec.defaults;

import com.cc.things.codec.Codec;
import com.cc.things.codec.Payload;
import com.gow.codec.bytes.BytesUtils;

import javax.annotation.Nonnull;

/**
 * long 类型编解码器
 * wcc 2022/6/4
 */
public class LongCodec implements Codec<Long> {

    public static LongCodec INSTANCE = new LongCodec();

    private LongCodec() {

    }

    @Override
    public Class<Long> forType() {
        return Long.class;
    }

    @Override
    public Long decode(@Nonnull Payload payload) {
        byte[] bytes = payload.getBytes(false);
        return BytesUtils.beToLong(bytes, 0, bytes.length);
    }

    @Override
    public Payload encode(Long body) {
        return Payload.of(BytesUtils.longToBe(body));
    }


}
