package com.cc.network.cp.domian;

/**
 * 编码器
 * wcc 2022/4/29
 */
public interface Decoder<T> {
    T decode(byte[] body);
}
