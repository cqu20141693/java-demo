package com.cc.network.cp.domian;

import com.cc.network.cp.domian.enums.MessageType;
import lombok.Builder;
import lombok.Data;

import static com.cc.network.cp.utils.DataParseUtils.bytesToShort;
import static com.cc.network.cp.utils.DataParseUtils.shortToBytes;

/**
 * {@link MessageType CHARGING_REPLY}
 * wcc 2022/4/29
 */
@Data
@Builder
public class ChargingReplyMessage implements Body {
    //WORD 对应的终端消息的流水号
    private short sequence;
    //BYTE 0：成功 1：失败
    private byte success;

    public ChargingReplyMessage(short sequence, byte success) {
        this.sequence = sequence;
        this.success = success;
    }

    @Override
    public MessageType getType() {
        return MessageType.CHARGING_REPLY;
    }

    @Override
    public byte[] encode() {
        byte[] bytes = shortToBytes(sequence);
        return new byte[]{bytes[0], bytes[1], success};
    }

    public static Body decode(byte[] body) {
        assert body.length == 3 : "body length error";
        return ChargingReplyMessage.builder()
                .sequence(bytesToShort(body[0], body[1]))
                .success(body[2])
                .build();
    }
}
