package com.gow.codec.base;

import java.nio.ByteBuffer;

public class TypeShortConvert implements TypeConversion<Short> {
    @Override
    public ConvertResponse<Short> rawDataConvert(byte[] payload) {
        ConvertResponse<Short> response = new ConvertResponse<>();
        if (payload == null || payload.length == 0 || payload.length > Short.BYTES) {
            return response.setSuccess(false).setFailMsg("payload length is not valid.");
        }

        ByteBuffer byteBuf = ByteBuffer.wrap(getBytes(payload));
        return response.setConvertResult(byteBuf.asShortBuffer().get());
    }

    private byte[] getBytes(byte[] payload) {
        byte[] bytes = new byte[2];
        if (payload.length == 1) {
            bytes[1] = payload[0];
            bytes[0] = 0;
        } else {
            bytes = payload;
        }
        return bytes;
    }

    @Override
    public Short strDataConvert(String data) {
        return Short.valueOf(data);
    }

    @Override
    public byte[] convertToBytes(Object obj) {
        Short intObj = (Short) obj;
        byte[] bytes = new byte[2];
        bytes[1] = (byte) (intObj & 0xff);
        bytes[0] = (byte) (intObj >> 8 & 0xff);
        return bytes;
    }

    @Override
    public String objectDataConvertStr(Object obj) {
        return obj.toString();
    }

    @Override
    public boolean validType(Object obj) {
        return obj instanceof Short;
    }
}
