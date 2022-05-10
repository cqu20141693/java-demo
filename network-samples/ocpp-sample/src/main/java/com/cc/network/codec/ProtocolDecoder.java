package com.cc.network.codec;

import com.cc.client.Utils;
import com.cc.network.cp.CPMessage;
import com.cc.network.cp.domian.enums.MessageType;
import com.cc.network.cp.utils.DataParseUtils;
import com.gow.codec.bytes.deserializable.DecodeContext;
import com.gow.codec.exception.DecodeException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

import static com.cc.network.codec.ProtocolEncoder.FIX_LENGTH;
import static com.gow.codec.bytes.DataType.OBJECT;

/**
 * 编解码处理器
 * wcc 2022/5/6
 */
@Data
@Slf4j
public class ProtocolDecoder extends MessageToMessageDecoder<ByteBuf> {
    public static final Integer FIX_HEADER_LEN = 13;

    @Override
    protected void decode(ChannelHandlerContext channelCtx, ByteBuf byteBuf, List<Object> list) throws Exception {
        int capacity = byteBuf.capacity();
        byte[] bytes = new byte[capacity];
        byteBuf.readBytes(bytes);
        byte[] unescape = unescape(bytes);
        validateCheckSum(unescape);
        DecodeContext context = OBJECT.deserialize(unescape, 0, 0, new CPMessage());
        CPMessage obj = (CPMessage) context.getObj();
        MessageType type = obj.getBody().getType();
        if (type == MessageType.LOGIN_REPLY) {

        } else if (type == MessageType.CHARGING) {
            log.info("handle charge message...");
            CPMessage reply = Utils.getDefaultChargingReply();
            channelCtx.channel().writeAndFlush(reply);
        }
        list.add(obj);
    }

    /**
     * 消息头，长度，body,checkSum
     *
     * @param input
     * @return
     */
    private byte[] unescape(byte[] input) {

        if (input[0] == 0x7e) {
            LinkedList<Byte> bytes = new LinkedList<>();
            // 0x7e<—— >0x7d 后紧跟一个 后紧跟一个 后紧跟一个 0x02
            //0x7d<—— >0x7d 后紧跟一个 后紧跟一个 后紧跟一个 0x01
            // 编码生成校验码，转义；解码先转义，再校验
            byte pre = 0;
            for (int index = 1; index < input.length - 1; ) {
                if (pre == 0x7d) {
                    if (input[index++] == 0x02) {
                        pre = 0x02;
                        bytes.removeLast();
                        bytes.add((byte) 0x7e);
                        continue;
                    } else if (input[index++] == 0x01) {
                        pre = 0x01;
                        continue;
                    } else {
                        throw new DecodeException("报文转义错误，index=" + index);
                    }
                }
                bytes.add(input[index++]);
                pre = input[index - 1];
            }

            int total = bytes.size() + 2;
            byte[] original = new byte[total];
            original[0] = input[0];
            for (int i = 0; i < bytes.size(); i++) {
                original[i + 1] = bytes.get(i);
            }
            original[total - 1] = input[input.length - 1];
            return original;
        } else {
            return input;
        }
    }

    private void validateCheckSum(byte[] original) {
        byte[] token = new byte[]{original[6], original[7]};
        byte[] body = new byte[original.length - FIX_LENGTH];
        System.arraycopy(original, FIX_HEADER_LEN, body, 0, body.length);
        if (!DataParseUtils.checkSum(body, token, original[original.length - 2])) {

            throw new DecodeException("check sum error");
        }
    }
}
