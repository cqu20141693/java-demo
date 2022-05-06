package com.gow.codec.bytes.deserializable;

/**
 * 字节反序列化器
 * wcc 2022/5/5
 */
public interface Deserializer {

    DecodeContext deserialize(byte[] bytes,int offset,int length,Object obj);
}
