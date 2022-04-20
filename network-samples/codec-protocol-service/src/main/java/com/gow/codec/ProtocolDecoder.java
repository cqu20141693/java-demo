package com.gow.codec;

/**
 * @author gow
 * @date 2021/9/22
 */
public interface ProtocolDecoder<T> {
    /**
     * 解码字节数组为模型
     *
     * @param bytes 字节数据
     * @return 模型
     */
    T decode(byte[] bytes);
}
