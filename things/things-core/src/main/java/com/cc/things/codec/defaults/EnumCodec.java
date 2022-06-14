package com.cc.things.codec.defaults;

import com.cc.things.codec.Codec;
import com.cc.things.codec.Payload;
import lombok.AllArgsConstructor;

import javax.annotation.Nonnull;
import java.util.Arrays;


/**
 * enum 类型编解码器
 * wcc 2022/6/4
 */
@AllArgsConstructor(staticName = "of")
public class EnumCodec<T extends Enum<?>> implements Codec<T> {

    private final T[] values;

    @Override
    @SuppressWarnings("all")
    public Class<T> forType() {
        return (Class<T>) values[0].getDeclaringClass();
    }

    @Override
    public T decode(@Nonnull Payload payload) {
        byte[] bytes = payload.getBytes(false);

        if (bytes.length > 0 && bytes[0] <= values.length) {
            return values[bytes[0] & 0xFF];
        }
        throw new IllegalArgumentException("can not decode payload " + Arrays.toString(bytes) + " to enums " + Arrays.toString(values));
    }

    @Override
    public Payload encode(T body) {
        return Payload.of(new byte[]{(byte) body.ordinal()});
    }


}
