package com.cc.things.codec.defaults;

import com.cc.things.codec.Codec;
import com.cc.things.codec.Payload;

import javax.annotation.Nonnull;

/**
 * Payload 类型编解码器
 * wcc 2022/6/4
 */
public class DirectCodec implements Codec<Payload> {

    public static final DirectCodec INSTANCE = new DirectCodec();

    public static <T extends Payload> Codec<T> instance() {
        return (Codec<T>) INSTANCE;
    }

    @Override
    public Class<Payload> forType() {
        return Payload.class;
    }

    @Override
    public Payload decode(@Nonnull Payload payload) {
        return payload;
    }

    @Override
    public Payload encode(Payload body) {
        return body;
    }
}
