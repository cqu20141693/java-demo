package com.cc.ocpp.network.codec;

import com.cc.ocpp.network.cp.CPMessage;
import com.cc.ocpp.network.cp.utils.DataParseUtils;
import com.gow.codec.bytes.deserializable.DecodeContext;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static com.cc.ocpp.network.codec.ProtocolEncoder.FIX_LENGTH;
import static com.gow.codec.bytes.DataType.OBJECT;

/**
 * 编解码处理器
 * wcc 2022/5/6
 */
@Slf4j
public class ProtocolDecoder extends MessageToMessageDecoder<ByteBuf> {
    public static final Integer FIX_HEADER_LEN = 13;

    @Override
    protected void decode(ChannelHandlerContext channelCtx, ByteBuf byteBuf, List<Object> list) throws Exception {

        int capacity = byteBuf.capacity();

        if (capacity == 1) {
            byte[] bytes = new byte[capacity];
            byteBuf.readBytes(bytes);
            if (bytes[0] != 0x7e) {
                log.info("flag not 0x7e:{}", bytes[0]);
            } else {
                log.info("read flag 0x7e");
            }
        } else {
            byte[] bytes = new byte[capacity + 1];
            bytes[0] = 0x7e;
            byteBuf.readBytes(bytes, 1, capacity);
            byte[] unescape = unescape(bytes);
            validateCheckSum(bytes);
            try {
                DecodeContext context = OBJECT.deserialize(bytes, 0, 0, new CPMessage());
                list.add(context);
            } catch (Exception e) {
                log.info("decode failed,e={}", e.getCause());
                list.add(DecodeContext.builder().success(false).build());
            }
        }

    }

    public static byte[] range(byte[] src, int start, int end) {
        if (src == null || src.length == 0 || end < start) return new byte[0];

        byte[] data = new byte[end - start + 1];
        System.arraycopy(src, start, data, 0, end - start + 1);
        return data;
    }

    /**
     * 消息头，长度，body,checkSum
     *
     * @param src
     * @return
     */
    private byte[] unescape(byte[] src) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            outputStream.write(0x7e);

            byte[] range = range(src, 1, src.length - 2);

            for (int i = 0; i < range.length; i++) {
                if (range[i] == 0x7d && ((i + 1) < range.length && range[i + 1] == 0x002)) {
                    outputStream.write(0x7e);
                    i++;
                } else if (range[i] == 0x7d && ((i + 1) < range.length && range[i + 1] == 0x001)) {
                    outputStream.write(0x7d);
                    i++;
                } else {
                    outputStream.write(range[i]);
                }
            }

            outputStream.write(0x7e);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    private void validateCheckSum(byte[] original) {
        byte[] token = new byte[]{original[6], original[7]};
        byte[] body = new byte[original.length - FIX_LENGTH];
        System.arraycopy(original, FIX_HEADER_LEN, body, 0, body.length);
        if (!DataParseUtils.checkSum(body, token, original[original.length - 2])) {
            log.info("check sum error");
            // throw new DecodeException("check sum error");
        }
    }
}
