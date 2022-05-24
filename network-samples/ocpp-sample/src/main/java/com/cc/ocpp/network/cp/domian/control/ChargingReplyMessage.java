package com.cc.ocpp.network.cp.domian.control;

import com.cc.ocpp.network.cp.domian.Body;
import com.cc.ocpp.network.cp.domian.FunctionReply;
import com.cc.ocpp.network.cp.domian.enums.MessageType;
import com.gow.codec.bytes.DataType;
import com.gow.codec.bytes.serializable.ObjectField;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.cc.ocpp.network.cp.utils.DataParseUtils.bytesToShort;
import static com.cc.ocpp.network.cp.utils.DataParseUtils.shortToBytes;

/**
 * {@link MessageType CHARGING_REPLY}
 * wcc 2022/4/29
 */
@Data
@Builder
@NoArgsConstructor
public class ChargingReplyMessage implements Body, FunctionReply {
    //WORD 对应的终端消息的流水号
    @ObjectField(dataType = DataType.SHORT)
    private Short sequence;
    //BYTE 0：成功 1：失败
    @ObjectField(dataType = DataType.BYTE)
    private Byte success;

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
