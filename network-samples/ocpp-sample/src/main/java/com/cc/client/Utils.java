package com.cc.client;

import com.cc.network.cp.CPMessage;
import com.cc.network.cp.Header;
import com.cc.network.cp.domian.enums.GunType;
import com.cc.network.cp.domian.enums.MessageType;
import com.cc.network.cp.domian.heart.GunData;
import com.cc.network.cp.domian.heart.PingMessage;
import com.gow.codec.bytes.deserializable.DecodeContext;
import lombok.Data;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static com.gow.codec.bytes.DataType.OBJECT;

/**
 * wcc 2022/5/6
 */
@Data
public class Utils {

    private static AtomicInteger counter = new AtomicInteger(0);

    public static CPMessage getDefaultLoginMessage() {
        String loginStr = "7E 00 01 00 00 00 00 00 01 02 13 00 9c 38 36 37 35 34 32 30 35 35 37 37 32 37 31 32 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 02 00 5A 31 30 34 02 01 01 0E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 35 30 32 45 00 00 00 00 00 00 41 39 42 32 43 34 32 00 00 00 38 39 38 36 30 34 37 34 30 39 32 31 38 30 37 39 35 34 31 31 00 00 00 00 34 36 30 30 34 39 34 34 35 39 30 35 34 31 31 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 54 7E";
        // String loginStr="7E 00 01 00 00 00 00 00 01 02 13 00 88 38 36 37 35 34 32 30 35 35 37 37 32 37 31 32 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 02 00 5A 31 30 34 02 01 01 0E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 35 30 32 45 00 00 00 00 00 00 41 39 42 32 43 34 32 00 00 00 38 39 38 36 30 34 37 34 30 39 32 31 38 30 37 39 35 34 31 31 00 00 00 00 34 36 30 30 34 39 34 34 35 39 30 35 34 31 31 00 54 7E";
        DecodeContext decodeContext = getCPMessage(loginStr);

        return (CPMessage) decodeContext.getObj();
    }

    public static CPMessage getDefaultPing() {
        String pingStr="7E 00 03 00 26 00 01 01 01 02 13 00 26 00 00 00 00 00 00 00 00 01 01 01 00 00 00 00 80 00 00 00 03 08 A2 00 00 00 00 00 06 1A 80 00 06 1A 80 00 06 1A 80 B4 7E";
        DecodeContext cpMessage = getCPMessage(pingStr);
        return (CPMessage) cpMessage.getObj();
    }
    public static CPMessage getDefaultEnableCharging(){
        String charging="7E 00 32 9B 49 00 01 01 01 01 6A 00 07 01 01 01 00 00 00 00 01 7E";
        DecodeContext cpMessage = getCPMessage(charging);
        return (CPMessage) cpMessage.getObj();
    }
    public static CPMessage getDefaultStopCharging(){
        String charging="7E 00 32 9B 4F 00 01 01 01 01 6A 00 07 01 00 01 00 00 00 00 00 7E";
        DecodeContext cpMessage = getCPMessage(charging);
        return (CPMessage) cpMessage.getObj();
    }

    public static DecodeContext getCPMessage(String loginStr) {
        String[] split = loginStr.split(" ");
        // String prefix = "0x";
        byte[] bytes = new byte[split.length];
        for (int i = 0; i < split.length; i++) {
            bytes[i] = (byte) Short.parseShort(split[i], 16);
        }
        DecodeContext decodeContext = OBJECT.deserialize(bytes, 0, 0, new CPMessage());
        return decodeContext;
    }

    public static PingMessage getPingMessage() {
        PingMessage ping = new PingMessage();
        ping.setStatus(1);
        ping.setWarnSignal(1);
        int nextInt = new Random().nextInt() & 0x02;
        ping.setGunNums((byte) nextInt);
        ArrayList<GunData> gunData = new ArrayList<>();
        for (int i = 0; i < nextInt; i++) {
            GunData data = new GunData();
            data.setGunType(GunType.single_phase_ac.getCode());
        }
        return ping;
    }

    public static CPMessage getDefaultMessage(MessageType messageType, Session session) {
        CPMessage cpMessage = new CPMessage();
        cpMessage.setFlag((byte) 0);
        Header header = new Header();
        header.setMessageId(messageType.getMessageId());
        short seq = (short) (counter.getAndIncrement() & 0xffff);
        header.setSequence(seq);
        header.setEncryption((byte) 0);
        header.setToken(session.getToken());
        header.setVersion(session.getVersion());
        cpMessage.setHeader(header);
        cpMessage.setEndFlag((byte) 0);
        return cpMessage;
    }
}
