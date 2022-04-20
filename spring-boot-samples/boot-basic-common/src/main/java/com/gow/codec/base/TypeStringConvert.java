package com.gow.codec.base;

import java.nio.charset.StandardCharsets;

public class TypeStringConvert implements TypeConversion<String> {
    @Override
    public ConvertResponse<String> rawDataConvert(byte[] payload) {
        ConvertResponse<String> response = new ConvertResponse<>();
        return response.setConvertResult(new String(payload, StandardCharsets.UTF_8));
    }

    @Override
    public String strDataConvert(String data) {
        return data;
    }

    @Override
    public byte[] convertToBytes(Object obj) {
        return ((String) obj).getBytes();
    }

    @Override
    public String objectDataConvertStr(Object obj) {
        return obj.toString();
    }

    @Override
    public boolean validType(Object obj) {
        return obj instanceof String;
    }
}
