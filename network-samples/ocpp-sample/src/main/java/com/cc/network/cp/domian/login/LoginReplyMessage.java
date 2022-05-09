package com.cc.network.cp.domian.login;

import com.cc.network.cp.domian.Body;
import com.cc.network.cp.domian.TokenGenerator;
import com.cc.network.cp.domian.enums.MessageType;
import com.gow.codec.bytes.DataType;
import com.gow.codec.bytes.serializable.ObjectField;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.cc.network.cp.utils.DataParseUtils.*;

/**
 * 登录响应消息
 * wcc 2022/4/25
 */
@Data
@Builder
@NoArgsConstructor
public class LoginReplyMessage implements Body {
    @ObjectField(dataType = DataType.SHORT)
    private Short sequence;
    //BYTE 0：成功/确认 1：失败 -- 平台记录此次登录，充电桩红灯，并在60秒后重新登录测试
    // 4：无相关联的桩或桩静态数据 5：失败—蓝牙未绑定过。
    @ObjectField(dataType = DataType.BYTE)
    private Byte success;
    // WORD
    @ObjectField(dataType = DataType.SHORT)
    private Short token;
    // DWORD 到当前的秒数，可做时间同步用
    @ObjectField(dataType = DataType.INT)
    private Integer timeSeconds;
    // WORD 心跳秒
    @ObjectField(dataType = DataType.SHORT)
    private Short keepalive;
    // WORD 充电实时数据间隔
    @ObjectField(dataType = DataType.SHORT)
    private Short reportInterval;
    // WORD[48] 区间电价 30 分钟一个区间
    @ObjectField(loop = 48)
    private Short[] intervalPrice;

    private static final int total = 109;

    public LoginReplyMessage(short sequence, byte success, short token, int timeSeconds, short keepalive, short reportInterval, Short[] intervalPrice) {
        this.sequence = sequence;
        this.success = success;
        assert TokenGenerator.validate(token) : "token value error";
        this.token = token;
        this.timeSeconds = timeSeconds;
        this.keepalive = keepalive;
        this.reportInterval = reportInterval;
        this.intervalPrice = intervalPrice;
    }

    public static Short[] getShorts(short[] intervalPrice) {
        Short[] shorts = new Short[intervalPrice.length];
        for (int i = 0; i < intervalPrice.length; i++) {
            shorts[i] = intervalPrice[i];
        }
        return shorts;
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
                .intervalPrice(getShorts(bytesToShorts(body, index, total)))
                .build();
    }

}
