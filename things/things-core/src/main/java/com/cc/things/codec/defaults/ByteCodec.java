package com.cc.things.codec.defaults;

import com.cc.things.codec.Codec;
import com.cc.things.codec.Payload;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nonnull;

/**
 * byte 类型编解码器
 * wcc 2022/6/4
 */
public class ByteCodec implements Codec<Byte> {

    public static ByteCodec INSTANCE = new ByteCodec();

    private ByteCodec() {

    }

    @Override
    public Class<Byte> forType() {
        return Byte.class;
    }

    @Override
    public Byte decode(@Nonnull Payload payload) {
        ByteBuf buf = payload.getBody();
        byte val = buf.getByte(0);
        buf.resetReaderIndex();
        return val;
    }

    @Override
    public Payload encode(Byte body) {
        return Payload.of(new byte[]{body});
    }


}
