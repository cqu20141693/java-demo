package com.cc.ocpp.network.codec;

import com.cc.ocpp.network.cp.CPMessage;
import com.cc.ocpp.network.cp.utils.DataParseUtils;
import com.gow.codec.bytes.BytesUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static com.gow.codec.bytes.DataType.OBJECT;

public class ProtocolEncoder extends MessageToMessageEncoder<CPMessage> {

    public static final Integer FIX_LENGTH = 15;

    @Override
    protected void encode(ChannelHandlerContext ctx, CPMessage message, List<Object> out) throws Exception {

        byte[] encodes = OBJECT.serialize(message);
        byte[] checkSum = setLengthAndCheckSum(encodes);
        byte[] escape = escape(checkSum);
        out.add(Unpooled.wrappedBuffer(escape));
    }

    private byte[] setLengthAndCheckSum(byte[] encodes) {
        // setLength
        short bodyLen = (short) (encodes.length - FIX_LENGTH);
        byte[] bytes = BytesUtils.shortToBe(bodyLen);
        encodes[11] = bytes[0];
        encodes[12] = bytes[1];
        // setCheckSum
        byte[] token = new byte[]{encodes[6], encodes[7]};
        byte[] body = new byte[bodyLen];
        System.arraycopy(encodes, FIX_LENGTH - 2, body, 0, bodyLen);
        encodes[encodes.length - 2] = DataParseUtils.getSum(body, token);
        return encodes;
    }

    /**
     * 消息头，长度，body,checkSum
     *
     * @param encodes
     * @return
     */
    private byte[] escape(byte[] encodes) {
        if (encodes[0] == 0x7e) {
            try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
                output.write(0x7e);
                for (int i = 1; i < encodes.length - 1; i++) {
                    if (encodes[i] == 0x7e) {
                        output.write(new byte[]{0x7d, 0x002});
                    } else if (encodes[i] == 0x7d) {
                        output.write(new byte[]{0x7d, 0x001});
                    } else {
                        output.write(encodes[i]);
                    }
                }
                output.write(0x7e);
                return output.toByteArray();
            } catch (IOException e) {
                throw new UnsupportedOperationException(e);
            }
        } else {
            return encodes;
        }
    }
}
