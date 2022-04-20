package com.gow.codec.base;

import java.util.Base64;

public class TypeBinConvert implements TypeConversion<byte[]> {
    @Override
    public ConvertResponse<byte[]> rawDataConvert(byte[] payload) {
        return new ConvertResponse<byte[]>().setConvertResult(payload);
    }

    @Override
    public byte[] strDataConvert(String data) {
        return Base64.getDecoder().decode(data);
    }

    @Override
    public byte[] convertToBytes(Object obj) {
        return (byte[]) obj;
    }

    @Override
    public String objectDataConvertStr(Object obj) {
        return new String(Base64.getEncoder().encode((byte[]) obj));
    }

    @Override
    public boolean validType(Object obj) {
        return obj instanceof byte[];
    }
}
