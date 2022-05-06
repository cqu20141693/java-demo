package com.gow.codec.bytes.serializable;

import com.alibaba.fastjson.JSONObject;
import com.gow.codec.bytes.deserializable.DecodeContext;
import com.gow.codec.bytes.model.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;

import static com.gow.codec.bytes.DataType.OBJECT;

@Slf4j
class DataTypeTest {

    @Test
    public void testObject() {
        PongMessage pongMessage = new PongMessage();
        // 2
        pongMessage.setSequence((short) 1);
        // 1
        pongMessage.setSuccess((byte) 1);
        //4
        pongMessage.setTime((int) (System.currentTimeMillis() / 1000));
        //1
        pongMessage.setType(ChargePointType.direct);
        // 32
        pongMessage.setChargePointId(RandomStringUtils.randomAlphabetic(32));
        byte[] bytes = OBJECT.serialize(pongMessage);
        assert bytes.length == 40 : "serialize error";
    }

    @Test
    public void testObjectNested() {
        encode();
    }

    private byte[] encode() {
        PingMessage msg = new PingMessage();
        //4
        msg.setStatus(1);
        //4
        msg.setWarnSignal((short) 1);
        //1
        msg.setGunNums((byte) 1);
        msg.setDescription(RandomStringUtils.randomAlphabetic(8));
        ArrayList<GunData> list = new ArrayList<>();
        GunData gunData = new GunData();
        // 1
        gunData.setGunType(GunType.single_phase_ac.getCode());
        // 1
        gunData.setWorkStatus((byte) 1);
        // 4
        gunData.setWarnSignal(1);
        // 4
        gunData.setSwitchSignal(1);
        SingleACField acField = new SingleACField();
        // 2
        acField.setU((short) 220);
        // 2
        acField.setI((short) 20);
        // 4
        acField.setMeterReading(1024);
        gunData.setData(acField);
        list.add(gunData);
        msg.setGunData(list);
        byte[] bytes = OBJECT.serialize(msg);
        assert bytes.length == 33 : "serialize error";
        return bytes;
    }

    @SneakyThrows
    @Test
    public void test() {
        try {
            GunType hello = GunType.valueOf("hello");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        String methodName = "valueOf";
        String name = "direct";
        ChargePointType direct = GunType.valueOf(ChargePointType.class, name);

        Class<ChargePointType> aClass = ChargePointType.class;

        Method method = aClass.getMethod(methodName, String.class);
        Object invoke = method.invoke(null, name);
        System.out.println(invoke);
    }

    @Test
    public void testDecode() {
        byte[] encode = encode();
        PingMessage message = new PingMessage();
        DecodeContext context = OBJECT.deserialize(encode, 0, encode.length, message);
        log.info("context obj={},offset={}", JSONObject.toJSONString(context.getObj()),context.getOffset());
    }
}
