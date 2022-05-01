package com.cc.network.cp.domian;

import com.cc.network.cp.domian.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import static com.cc.network.cp.utils.DataParseUtils.intToBytes;
import static com.cc.network.cp.utils.DataParseUtils.parseUnsignedBytes;

/**
 * {@link MessageType CHARGING}
 * wcc 2022/4/29
 */
@Data
@Builder
public class ChargingMessage implements Body {
    //充电枪端口号
    private byte port;
    //0 –停止充电 1- 开始充电
    private byte status;
    // 1 – 充满为止 2 - 按金额充 3 – 按电量冲 4 – 按时间充
    private byte mode;
    // DWORD
    //充满：填0 金额：精确到0.01元 电量：精确到0.001度 o时间：精确到分
    private int value;

    public ChargingMessage(byte port, byte status, byte mode, int value) {
        this.port = port;
        this.status = status;
        this.mode = mode;
        this.value = value;
    }

    @Override
    public MessageType getType() {
        return MessageType.CHARGING;
    }

    @Override
    public byte[] encode() {
        byte[] bytes = intToBytes(value);
        return new byte[]{port, status, mode, bytes[0], bytes[1], bytes[2], bytes[3]};
    }

    public static Body decode(byte[] body) {

        assert body.length == 7 : "body length error";
       return ChargingMessage.builder().port( body[0])
                .status( body[1])
                .mode( body[2])
                .value((int) (parseUnsignedBytes(new byte[]{body[3], body[4], body[5], body[6]})))
                .build();
    }
}
