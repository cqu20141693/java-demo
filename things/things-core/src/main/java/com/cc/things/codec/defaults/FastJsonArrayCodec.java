package com.cc.things.codec.defaults;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cc.things.codec.Codec;
import com.cc.things.codec.Payload;

import javax.annotation.Nonnull;

/**
 * JSONArray 类型编解码器
 * wcc 2022/6/4
 */
public class FastJsonArrayCodec implements Codec<JSONArray> {

    public static final FastJsonArrayCodec INSTANCE = new FastJsonArrayCodec();

    @Override
    public Class<JSONArray> forType() {
        return JSONArray.class;
    }

    @Override
    public JSONArray decode(@Nonnull Payload payload) {
        return JSON.parseArray(payload.bodyToString(false));
    }

    @Override
    public Payload encode(JSONArray body) {
        return Payload.of(JSON.toJSONBytes(body));
    }

}
