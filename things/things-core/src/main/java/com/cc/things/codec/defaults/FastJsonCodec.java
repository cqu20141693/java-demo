package com.cc.things.codec.defaults;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cc.things.codec.Codec;
import com.cc.things.codec.Payload;

import javax.annotation.Nonnull;

/**
 * JSNOObject 类型编解码器
 * wcc 2022/6/4
 */
public class FastJsonCodec implements Codec<JSONObject> {

    public static final FastJsonCodec INSTANCE = new FastJsonCodec();

    @Override
    public Class<JSONObject> forType() {
        return JSONObject.class;
    }

    @Override
    public JSONObject decode(@Nonnull Payload payload) {
        return JSON.parseObject(payload.bodyToString(false));
    }

    @Override
    public Payload encode(JSONObject body) {
        return Payload.of(JSON.toJSONBytes(body));
    }

}
