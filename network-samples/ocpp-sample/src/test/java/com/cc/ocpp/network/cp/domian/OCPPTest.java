package com.cc.ocpp.network.cp.domian;

import com.alibaba.fastjson.JSONObject;
import com.cc.ocpp.network.cp.CPMessage;
import com.cc.ocpp.network.cp.domian.login.LoginMessage;
import com.gow.codec.bytes.deserializable.DecodeContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static com.cc.ocpp.client.Utils.*;
import static com.gow.codec.bytes.DataType.OBJECT;

@Slf4j
class OCPPTest {

    @Test
    public void testLogin() {
        String loginStr = "7E 00 01 00 00 00 00 00 01 02 13 00 9c 38 36 37 35 34 32 30 35 35 37 37 32 37 31 32 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 02 00 5A 31 30 34 02 01 01 0E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 35 30 32 45 00 00 00 00 00 00 41 39 42 32 43 34 32 00 00 00 38 39 38 36 30 34 37 34 30 39 32 31 38 30 37 39 35 34 31 31 00 00 00 00 34 36 30 30 34 39 34 34 35 39 30 35 34 31 31 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 54 7E";
        // String loginStr="7E 00 01 00 00 00 00 00 01 02 13 00 88 38 36 37 35 34 32 30 35 35 37 37 32 37 31 32 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 02 00 5A 31 30 34 02 01 01 0E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 35 30 32 45 00 00 00 00 00 00 41 39 42 32 43 34 32 00 00 00 38 39 38 36 30 34 37 34 30 39 32 31 38 30 37 39 35 34 31 31 00 00 00 00 34 36 30 30 34 39 34 34 35 39 30 35 34 31 31 00 54 7E";
        String[] split = loginStr.split(" ");
        // String prefix = "0x";
        byte[] bytes = new byte[split.length];
        for (int i = 0; i < split.length; i++) {
            bytes[i] = (byte) Short.parseShort(split[i], 16);
        }
        DecodeContext decodeContext = OBJECT.deserialize(bytes, 0, 0, new CPMessage());
        CPMessage obj = (CPMessage) decodeContext.getObj();
        LoginMessage login = (LoginMessage) obj.getBody();
        byte[] serialize = OBJECT.serialize(obj);
        log.info("message={}", JSONObject.toJSONString(login));
    }

    @Test
    public void testPing() {
        CPMessage defaultPing = getDefaultPing(0);
        byte[] serialize = OBJECT.serialize(defaultPing);
        System.out.println(JSONObject.toJSONString(defaultPing));
    }
    @Test
    public void testCharging() {
        CPMessage enable = getDefaultEnableCharging();
        byte[] serialize = OBJECT.serialize(enable);
        System.out.println(JSONObject.toJSONString(enable));
        CPMessage stop = getDefaultStopCharging();
        byte[] serialize1 = OBJECT.serialize(stop);
        System.out.println(JSONObject.toJSONString(stop));
    }

    @Test
    public void testChargingReply() {
        CPMessage reply = getDefaultChargingReply();
        byte[] serialize = OBJECT.serialize(reply);
        log.info("testChargingReply={}", JSONObject.toJSONString(reply.getBody()));
    }

    @Test
    public void testLoginReply() {
        CPMessage reply = getDefaultLoginReply();
        byte[] serialize = OBJECT.serialize(reply);
        log.info("testLoginReply={}", JSONObject.toJSONString(reply.getBody()));
    }

}
