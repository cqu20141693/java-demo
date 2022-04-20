package com.gow.codec.base;

public interface TypeConversion<T> {
    ConvertResponse<T> rawDataConvert(byte[] payload);

    T strDataConvert(String data);

    byte[] convertToBytes(Object obj);

    String objectDataConvertStr(Object obj);

    boolean validType(Object obj);
}
