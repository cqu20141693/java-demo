package com.cc.things.codec.defaults;

import com.cc.things.codec.Codec;
import com.cc.things.codec.Payload;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nonnull;

/**
 * ByteBuf类型编解码器
 * wcc 2022/6/4
 */
public class ByteBufCodec implements Codec<ByteBuf> {

    public static final ByteBufCodec INSTANCE = new ByteBufCodec();

    @Override
    public Class<ByteBuf> forType() {
        return ByteBuf.class;
    }

    @Override
    public ByteBuf decode(@Nonnull Payload payload) {
        return payload.getBody();
    }

    @Override
    public Payload encode(ByteBuf body) {
        return Payload.of(body);
    }
}
