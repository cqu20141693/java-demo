package com.gow.codec.base;

import java.nio.ByteBuffer;

public class TypeIntConvert implements TypeConversion<Integer> {
    @Override
    public ConvertResponse<Integer> rawDataConvert(byte[] payload) {
        ConvertResponse<Integer> response = new ConvertResponse<>();
        if (payload == null || payload.length == 0 || payload.length > Integer.BYTES) {
            return response.setSuccess(false).setFailMsg("payload length is not valid.");
        }

        ByteBuffer byteBuf = ByteBuffer.wrap(getBytes(payload));
        return response.setConvertResult(byteBuf.asIntBuffer().get());
    }

    private byte[] getBytes(byte[] payload) {
        byte[] bytes = new byte[4];
        if (payload.length == 1) {
            bytes[0] = 0;
            bytes[1] = 0;
            bytes[2] = 0;
            bytes[3] = payload[0];
        } else if (payload.length == 2) {
            bytes[0] = 0;
            bytes[1] = 0;
            bytes[2] = payload[0];
            bytes[3] = payload[1];
        } else if (payload.length == 3) {
            bytes[0] = 0;
            bytes[1] = payload[0];
            bytes[2] = payload[1];
            bytes[3] = payload[2];
        } else {
            bytes = payload;
        }
        return bytes;
    }

    @Override
    public Integer strDataConvert(String data) {
        return Integer.valueOf(data);
    }

    @Override
    public byte[] convertToBytes(Object obj) {
        Integer intObj = (Integer) obj;
        byte[] bytes = new byte[4];
        bytes[3] = (byte) (intObj & 0xff);
        bytes[2] = (byte) (intObj >> 8 & 0xff);
        bytes[1] = (byte) (intObj >> 16 & 0xff);
        bytes[0] = (byte) (intObj >>> 24);
        return bytes;
    }

    @Override
    public String objectDataConvertStr(Object obj) {
        return obj.toString();
    }

    @Override
    public boolean validType(Object obj) {
        return obj instanceof Integer;
    }
}
