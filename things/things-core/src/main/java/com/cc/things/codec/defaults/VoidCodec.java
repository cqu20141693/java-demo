package com.cc.things.codec.defaults;


import com.cc.things.codec.Codec;
import com.cc.things.codec.Payload;

import javax.annotation.Nonnull;

public class VoidCodec implements Codec<Void> {

    public static VoidCodec INSTANCE = new VoidCodec();

    @Override
    public Class<Void> forType() {
        return Void.class;
    }

    @Override
    public Void decode(@Nonnull Payload payload) {
        return null;
    }

    @Override
    public Payload encode(Void body) {

        return Payload.of(new byte[0]);
    }

    @Override
    public boolean isDecodeFrom(Object nativeObject) {
        return nativeObject == null;
    }
}
