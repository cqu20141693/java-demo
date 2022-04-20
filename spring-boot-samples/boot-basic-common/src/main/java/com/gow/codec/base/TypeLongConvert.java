package com.gow.codec.base;

import java.nio.ByteBuffer;

public class TypeLongConvert implements TypeConversion<Long> {
    @Override
    public ConvertResponse<Long> rawDataConvert(byte[] payload) {
        ConvertResponse<Long> response = new ConvertResponse<>();
        if (payload == null || payload.length == 0 || payload.length > Long.BYTES) {
            return response.setSuccess(false).setFailMsg("payload length is not valid.");
        }
        byte[] bytes = new byte[8];
        bytes = getBytes(payload, bytes);

        ByteBuffer byteBuf = ByteBuffer.wrap(bytes);
        return response.setConvertResult(byteBuf.asLongBuffer().get());
    }

    private byte[] getBytes(byte[] payload, byte[] bytes) {
        if (payload.length == 1) {
            bytes[7] = payload[0];
            bytes[6] = 0;
            bytes[5] = 0;
            bytes[4] = 0;
            bytes[3] = 0;
            bytes[2] = 0;
            bytes[1] = 0;
            bytes[0] = 0;
        } else if (payload.length == 2) {
            bytes[7] = payload[0];
            bytes[6] = payload[1];
            bytes[5] = 0;
            bytes[4] = 0;
            bytes[3] = 0;
            bytes[2] = 0;
            bytes[1] = 0;
            bytes[0] = 0;
        } else if (payload.length == 3) {
            bytes[7] = payload[0];
            bytes[6] = payload[1];
            bytes[5] = payload[2];
            bytes[4] = 0;
            bytes[3] = 0;
            bytes[2] = 0;
            bytes[1] = 0;
            bytes[0] = 0;
        } else if (payload.length == 4) {
            bytes[7] = payload[0];
            bytes[6] = payload[1];
            bytes[5] = payload[2];
            bytes[4] = payload[3];
            bytes[3] = 0;
            bytes[2] = 0;
            bytes[1] = 0;
            bytes[0] = 0;
        } else if (payload.length == 5) {
            bytes[7] = payload[0];
            bytes[6] = payload[1];
            bytes[5] = payload[2];
            bytes[4] = payload[3];
            bytes[3] = payload[4];
            bytes[2] = 0;
            bytes[1] = 0;
            bytes[0] = 0;
        } else if (payload.length == 6) {
            bytes[7] = payload[0];
            bytes[6] = payload[1];
            bytes[5] = payload[2];
            bytes[4] = payload[3];
            bytes[3] = payload[4];
            bytes[2] = payload[5];
            bytes[1] = 0;
            bytes[0] = 0;
        } else if (payload.length == 7) {
            bytes[7] = payload[0];
            bytes[6] = payload[1];
            bytes[5] = payload[2];
            bytes[4] = payload[3];
            bytes[3] = payload[4];
            bytes[2] = payload[5];
            bytes[1] = payload[6];
            bytes[0] = 0;
        } else {
            bytes = payload;
        }
        return bytes;
    }

    @Override
    public Long strDataConvert(String data) {
        return Long.valueOf(data);
    }

    @Override
    public byte[] convertToBytes(Object obj) {
        Long longObj = (Long) obj;
        byte[] bytes = new byte[8];
        bytes[7] = (byte) (longObj & 0xff);
        bytes[6] = (byte) ((longObj >> 8) & 0xff);
        bytes[5] = (byte) ((longObj >> 16) & 0xff);
        bytes[4] = (byte) ((longObj >> 24) & 0xff);
        bytes[3] = (byte) ((longObj >> 32) & 0xff);
        bytes[2] = (byte) ((longObj >> 40) & 0xff);
        bytes[1] = (byte) ((longObj >> 48) & 0xff);
        bytes[0] = (byte) ((longObj >> 56) & 0xff);
        return bytes;
    }

    /**
     * 将字节数组转为long<br>
     * 如果input为null,或offset指定的剩余数组长度不足8字节则抛出异常
     *
     * @param input
     * @param offset       起始偏移量
     * @param littleEndian 输入数组是否小端模式
     * @return
     */
    public static long longFromBytes(byte[] input, int offset, boolean littleEndian) {
        long value = 0;
        // 循环读取每个字节通过移位运算完成long的8个字节拼装
        for (int count = 0; count < 8; ++count) {
            int shift = (littleEndian ? count : (7 - count)) << 3;
            value |= ((long) 0xff << shift) & ((long) input[offset + count] << shift);
        }
        return value;
    }

    @Override
    public String objectDataConvertStr(Object obj) {
        return obj.toString();
    }

    @Override
    public boolean validType(Object obj) {
        return obj instanceof Long;
    }
}
