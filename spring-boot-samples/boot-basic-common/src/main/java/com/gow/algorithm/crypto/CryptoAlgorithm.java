package com.gow.algorithm.crypto;

/**
 * @author gow
 * @date 2021/6/28 0028
 */
public interface CryptoAlgorithm {
    /**
     * 获取名称
     *
     * @return
     */
    String getName();

    /**
     * 具体的算法类型
     *
     * @return
     */
    String getType();

}
