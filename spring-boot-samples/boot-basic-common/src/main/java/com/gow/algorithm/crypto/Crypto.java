package com.gow.algorithm.crypto;

/**
 * @author gow
 * @date 2021/6/28 0028
 */
public interface Crypto {
    /**
     * 数据加密
     *
     * @param data
     * @return null 表示加密失败
     */
    byte[] encrypt(byte[] data);

    /**
     * 数据解密
     *
     * @param data
     * @return null 表示解密失败
     */
    byte[] decrypt(byte[] data);

}
