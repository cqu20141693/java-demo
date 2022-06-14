package com.cc.things.codec.defaults;

import com.alibaba.fastjson.JSON;
import com.cc.things.codec.Codec;
import com.cc.things.codec.Payload;

import javax.annotation.Nonnull;
/**
 * object 类型编解码器
 * wcc 2022/6/4
 */
public class JsonCodec<T> implements Codec<T> {

    private final Class<? extends T> type;

    private JsonCodec(Class<? extends T> type) {
        this.type = type;
    }

    public static <T> JsonCodec<T> of(Class<? extends T> type) {
        return new JsonCodec<>(type);
    }

    @Override
    public Class<T> forType() {
        return (Class<T>) type;
    }

    @Override
    public T decode(@Nonnull Payload payload) {
        return JSON.parseObject(payload.getBytes(false), type);
    }

    @Override
    public Payload encode(T body) {
        return Payload.of(JSON.toJSONBytes(body));
    }

}
