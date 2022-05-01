package com.cc.network.cp;

import com.alibaba.fastjson.JSONObject;
import com.cc.network.cp.domian.*;
import com.cc.network.cp.domian.enums.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
class CPMessageTest {
    private final AtomicInteger sequence = new AtomicInteger(0);
    private final TokenGenerator generator = new TokenGenerator();
    private final String KEY = "token";
    private final String keepaliveKey = "keepalive";
    private final String reportIntervalKey = "interval";
    private final String priceKey = "price";

    @Test
    void getPayload() {
        // testChargingMessage();
        testLoginMessage();
    }

    private void testLoginMessage() {

        String chargePointId = RandomStringUtils.randomAlphanumeric(32);
        ChargePointType type = ChargePointType.direct;
        Version ChargePointVersion = Version.default_version;
        ManufacturerInfo info = new ManufacturerInfo(ManufacturerType.ling_co, ProductType.alternate_private1, PowerType.unknown);
        ArrayList<ChargeGun> chargeGuns = new ArrayList<>();
        String gunId = RandomStringUtils.randomAlphanumeric(32);
        ChargeGun chargeGun = new ChargeGun(gunId, (byte) 0);
        chargeGuns.add(chargeGun);
        String community = RandomStringUtils.randomAlphanumeric(10);
        String baseStation = RandomStringUtils.randomAlphanumeric(10);
        String ICCID = RandomStringUtils.randomAlphanumeric(24);
        String IMDI = RandomStringUtils.randomAlphanumeric(16);
        String bluetooth = RandomStringUtils.randomAlphanumeric(20);
        Body message = LoginMessage.builder().chargePointId(chargePointId).type(type)
                .version(ChargePointVersion).manufacturer(info).chargeGuns(chargeGuns).network(NetworkType.fourG)
                .community(community).baseStation(baseStation).ICCID(ICCID).IMDI(IMDI).bluetooth(bluetooth)
                .build();
        byte flag = 0;
        byte encryption = 0;
        Version version = new Version((byte) 1, (byte) 2, (byte) 23);
        int seq = sequence.getAndIncrement();
        // login token==null
        testMessage(message, flag, encryption, null, version, seq);

    }

    private void testChargingMessage() {
        Body message = new ChargingMessage((byte) 1, (byte) 1, (byte) 1, 0);
        byte flag = 0;
        byte encryption = 0;
        Version version = Version.default_version;
        int seq = sequence.getAndIncrement();
        testMessage(message, flag, encryption, generator.generate(), version, seq);
    }

    private void testMessage(Body message, byte flag, byte encryption, Short token, Version version, int seq) {
        // client 构建message
        CPMessage cpMessage = new CPMessage(flag, message.getType(),
                seq, encryption, token, version, message);

        log.info("client send msg={}", JSONObject.toJSONString(cpMessage));
        // codec encode;
        byte[] payload = cpMessage.toByteBuf().array();

        CPMessage decodeMsg = CPMessage.decode(payload);

        MessageType receiveType = decodeMsg.getBody().getType();
        log.info("server receive msg={},type={}", JSONObject.toJSONString(decodeMsg), receiveType);

        // 响应消息应该是在session中处理消息后返回，
        HashMap<String, Object> session = createSession();
        if (decodeMsg.getBody().getType() == MessageType.LOGIN) {
            session.put(KEY, generator.generate());
        } else {
            session.put(KEY, token);
        }

        // getPriceByPlatform
        session.put(priceKey, getPriceByPlatform());
        Body reply = createReply(decodeMsg, (byte) 0, session);
        if (reply != null) {
            CPMessage replyMsg = new CPMessage(flag, reply.getType(), seq, encryption, (short)session.get(KEY), version, reply);
            log.info("server create and send reply={}", JSONObject.toJSONString(replyMsg));

            //client receive reply
            byte[] encodeReply = replyMsg.toByteBuf().array();
            CPMessage decodeReply = CPMessage.decode(encodeReply);
            log.info("client receive reply={},type={}", JSONObject.toJSONString(decodeReply), decodeReply.getBody().getType());
        }
    }

    private HashMap<String, Object> createSession() {
        HashMap<String, Object> session = new HashMap<>();
        session.put(keepaliveKey, (short) 120);
        session.put(reportIntervalKey, (short) 60);
        return session;
    }

    private short[] getPriceByPlatform() {
        short[] shorts = new short[48];
        for (int i = 0; i < 48; i++) {
            shorts[i] = 0;
        }
        return shorts;
    }

    private Body createReply(CPMessage decodeMsg, byte success, Map<String, Object> session) {
        switch (decodeMsg.getBody().getType()) {
            case LOGIN:
                return LoginReplyMessage.builder()
                        .sequence(decodeMsg.getHeader().getSequence())
                        .success(success)
                        .token((Short) session.get(KEY))
                        .timeSeconds((int) (System.currentTimeMillis() / 1000))
                        .keepalive((short) session.get(keepaliveKey))
                        .reportInterval((Short) session.get(reportIntervalKey))
                        .intervalPrice((short[]) session.get(priceKey))
                        .build();
            case CHARGING:
                return ChargingReplyMessage.builder()
                        .sequence(decodeMsg.getHeader().getSequence())
                        .success(success)
                        .build();
            default:
                return null;
        }
    }
}
