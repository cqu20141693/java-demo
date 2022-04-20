package com.gow.algorithm.signature;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author gow
 * @date 2021/6/28 0028
 */
public interface Signature {
    /**
     * 数据签名
     *
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    byte[] doSignature(byte[] data) ;
}
