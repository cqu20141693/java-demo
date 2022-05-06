package com.cc.network.cp.domian.enums;

import com.cc.network.cp.domian.Body;
import com.cc.network.cp.domian.control.ChargingMessage;
import com.cc.network.cp.domian.control.ChargingReplyMessage;
import com.cc.network.cp.domian.heart.PingMessage;
import com.cc.network.cp.domian.heart.PongMessage;
import com.cc.network.cp.domian.login.LoginMessage;
import com.cc.network.cp.domian.login.LoginMessageCopy;
import com.cc.network.cp.domian.login.LoginReplyMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * wcc 2022/4/29
 */
public enum MessageType {
    LOGIN((short) 0x0001, "充电桩登陆", LoginMessage::decode, LoginMessage.class),
    LOGIN_REPLY((short) 0x8001, "充电桩登陆应答", LoginReplyMessage::decode, LoginReplyMessage.class),
    PING((short) 0x0003, "充电桩心跳事件", PingMessage::decode, PingMessage.class),
    PONG((short) 0x8003, "充电桩心跳事件", PongMessage::decode, PongMessage.class),
    CHARGING((short) 0x0032, "开启/停止充电指令", ChargingMessage::decode, ChargingMessage.class),
    CHARGING_REPLY((short) 0x8032, "停止充电指令应答", ChargingReplyMessage::decode, ChargingReplyMessage.class),
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

    MessageType(Short messageId, String text, Function<byte[], Body> decoder, Class<?> zClass) {
        this.messageId = messageId;
        this.text = text;
        this.decoder = decoder;
        this.zClass = zClass;
    }

    private final Short messageId;
    private final String text;
    private final Function<byte[], Body> decoder;

    public Class<?> getzClass() {
        return zClass;
    }

    private final Class<?> zClass;

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
