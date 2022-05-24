package com.cc.netwok.utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wujt
 */
@Slf4j
public class NettyUtils {

    public static final String ATTR_CLIENT_ID = "clientId";
    public static final String ATTR_CHANNEL_STATE = "state";


    private static final AttributeKey<String> ATTR_KEY_CLIENT_ID = AttributeKey.valueOf(ATTR_CLIENT_ID);

    public static String strAttr(Channel channel, String key) {
        AttributeKey<String> attrKey = AttributeKey.valueOf(key);
        return channel.attr(attrKey).get();
    }

    public static Integer intAttr(Channel channel, String key) {
        AttributeKey<Integer> attrKey = AttributeKey.valueOf(key);
        return channel.attr(attrKey).get();
    }

    public static Boolean boolAttr(Channel channel, String key) {
        AttributeKey<Boolean> attrKey = AttributeKey.valueOf(key);
        return channel.attr(attrKey).get();
    }

    public static void attr(Channel channel, String key, Object value) {
        if (value instanceof Integer) {
            AttributeKey<Integer> attrKey = AttributeKey.valueOf(key);
            channel.attr(attrKey).set((Integer) value);
        } else if (value instanceof String) {
            AttributeKey<String> attrKey = AttributeKey.valueOf(key);
            channel.attr(attrKey).set((String) value);
        } else if (value instanceof Boolean) {
            AttributeKey<Boolean> attrKey = AttributeKey.valueOf(key);
            channel.attr(attrKey).set((Boolean) value);
        } else {
            log.info("not support value type");
        }

    }

    public static byte[] readBytesAndRewind(ByteBuf payload) {
        byte[] payloadContent = new byte[payload.readableBytes()];
        int mark = payload.readerIndex();
        payload.readBytes(payloadContent);
        payload.readerIndex(mark);
        return payloadContent;
    }
}
