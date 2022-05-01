package com.cc.network.cp.domian;

import com.cc.network.cp.domian.enums.MessageType;
import lombok.Builder;
import lombok.Data;

import static com.cc.network.cp.utils.DataParseUtils.*;

/**
 * 登录响应消息
 * wcc 2022/4/25
 */
@Data
@Builder
public class LoginReplyMessage implements Body {

    private short sequence;
    //BYTE 0：成功/确认 1：失败 -- 平台记录此次登录，充电桩红灯，并在60秒后重新登录测试
    // 4：无相关联的桩或桩静态数据 5：失败—蓝牙未绑定过。
    private byte success;
    // WORD
    private short token;
    // DWORD 到当前的秒数，可做时间同步用
    private int timeSeconds;
    // WORD 心跳秒
    private short keepalive;
    // WORD 充电实时数据间隔
    private short reportInterval;
    // WORD[48] 区间电价 30 分钟一个区间
    private short[] intervalPrice;

    private static final int total = 109;

    public LoginReplyMessage(short sequence, byte success, short token, int timeSeconds, short keepalive, short reportInterval, short[] intervalPrice) {
        this.sequence = sequence;
        this.success = success;
        assert TokenGenerator.validate(token) : "token value error";
        this.token = token;
        this.timeSeconds = timeSeconds;
        this.keepalive = keepalive;
        this.reportInterval = reportInterval;
        this.intervalPrice = intervalPrice;
    }

    @Override
    public MessageType getType() {
        return MessageType.LOGIN_REPLY;
    }

    @Override
    public byte[] encode() {
        byte[] body = new byte[total];
        int index = 0;
        index = putAndGetIndex(body, index, shortToBytes(sequence));
        body[index++] = success;
        index = putAndGetIndex(body, index, shortToBytes(token));
        index = putAndGetIndex(body, index, intToBytes(timeSeconds));
        index = putAndGetIndex(body, index, shortToBytes(keepalive));
        index = putAndGetIndex(body, index, shortToBytes(reportInterval));
        for (int i = 0; i < intervalPrice.length; i++) {
            index = putAndGetIndex(body, index, shortToBytes(intervalPrice[i]));
        }
        if (index == total) {
            return body;
        } else {
            throw new RuntimeException("编码索引错误");
        }
    }


    public static LoginReplyMessage decode(byte[] body) {
        assert body.length == total : "消息长度错误";
        int index = 0;
        return LoginReplyMessage.builder()
                .sequence(bytesToShort(body[index++], body[index++]))
                .success(body[index++])
                .token(bytesToShort(body[index++], body[index++]))
                .timeSeconds((int) (parseUnsignedBytes(new byte[]{body[index++], body[index++], body[index++], body[index++]})))
                .keepalive(bytesToShort(body[index++], body[index++]))
                .reportInterval(bytesToShort(body[index++], body[index++]))
                .intervalPrice(bytesToShorts(body,index,total))
                .build();
    }

}
