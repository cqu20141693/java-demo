package com.cc.things.codec.defaults;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cc.things.codec.Codec;
import com.cc.things.codec.Payload;
import com.cc.util.Function3;
import com.cc.util.StringUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import javax.annotation.Nonnull;
import java.util.function.Function;
/**
 * error 类型编解码器
 * wcc 2022/6/4
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorCodec implements Codec<Throwable> {

    public static ErrorCodec RUNTIME = of((type, msg, stack) -> stack == null ? new RuntimeException(msg) : new RuntimeException(stack));

    public static ErrorCodec DEFAULT = RUNTIME;

    private final Function3</*异常类型*/String,/*message*/ String,/*stack*/String, Throwable> mapping;

    public static ErrorCodec of(Function<String, Throwable> mapping) {
        return new ErrorCodec((type, msg, stack) -> mapping.apply(msg));
    }

    public static ErrorCodec of(Function3<String, String, String, Throwable> mapping) {
        return new ErrorCodec(mapping);
    }

    @Override
    public Class<Throwable> forType() {
        return Throwable.class;
    }

    @Override
    public Throwable decode(@Nonnull Payload payload) {
        String body = payload.bodyToString(false);
        if (body.startsWith("{")) {
            JSONObject json = JSON.parseObject(body);
            return mapping.apply(json.getString("t"), json.getString("m"), json.getString("s"));
        }
        return mapping.apply(null, body, null);
    }

    @Override
    public Payload encode(Throwable body) {
        JSONObject state = new JSONObject();
        state.put("m", body.getMessage());
        state.put("t", body.getClass().getName());
        state.put("s", StringUtils.throwable2String(body));
        return Payload.of(state.toJSONString());
    }
}
