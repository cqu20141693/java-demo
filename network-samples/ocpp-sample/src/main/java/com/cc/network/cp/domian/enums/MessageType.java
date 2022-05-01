package com.cc.network.cp.domian.enums;

import com.cc.network.cp.domian.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * wcc 2022/4/29
 */
public enum MessageType {
    LOGIN((short) 0x0001, "充电桩登陆", LoginMessage::decode),
    LOGIN_REPLY((short) 0x8001, "充电桩登陆应答", LoginReplyMessage::decode),
    CHARGING((short) 0x0032, "开启/停止充电指令", ChargingMessage::decode),
    CHARGING_REPLY((short) 0x8032, "停止充电指令应答", ChargingReplyMessage::decode),
    ;
    private static final Map<Short, MessageType> inner;

    static {
        inner = new HashMap<>();
        for (MessageType value : values()) {
            inner.put(value.messageId, value);
        }
    }

    public static MessageType parseByMessageId(short id) {
        return inner.get(id);
    }

    MessageType(Short messageId, String text, Function<byte[], Body> decoder) {
        this.messageId = messageId;
        this.text = text;
        this.decoder = decoder;
    }

    private final Short messageId;
    private final String text;
    private final Function<byte[], Body> decoder;

    public Short getMessageId() {
        return messageId;
    }
    public String getText() {
        return text;
    }

    public Function<byte[], Body> getDecoder() {
        return decoder;
    }
}
