package com.cc.things.codec.defaults;

import com.cc.things.codec.Codec;
import com.cc.things.codec.Payload;

import javax.annotation.Nonnull;
/**
 * bytes 类型编解码器
 * wcc 2022/6/4
 */
public class BytesCodec implements Codec<byte[]> {

    public static BytesCodec INSTANCE = new BytesCodec();

    private BytesCodec() {

    }

    @Override
    public Class<byte[]> forType() {
        return byte[].class;
    }

    @Override
    public byte[] decode(@Nonnull Payload payload) {
        return payload.getBytes(false);
    }

    @Override
    public Payload encode(byte[] body) {
        return Payload.of(body);
    }


}
