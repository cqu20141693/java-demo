package com.gow.codec;

/**
 * @author gow
 * @date 2021/9/22
 */
public interface ProtocolEncoder<T> {
    /**
     * 数据编码组装
     *
     * @return 编码二进制
     */
    byte[] encode(T t, Byte version);
}
