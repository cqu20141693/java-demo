package com.gow.algorithm.crypto;

/**
 * @author gow
 * @date 2021/6/28 0028
 */
public enum CBCSymmetricCryptoAlgorithm implements CryptoAlgorithm {


    AES_CBC("AES", "AES/CBC/PKCS5Padding"),
    DES_CBC("DES", "DES/CBC/PKCS5Padding"),
    SM4_CBC("SM4", "SM4/CBC/PKCS5Padding"),
    SM4_CBC_V2("SM4", "SM4/CBC/PKCS7Padding"),

    UNKNOWN("unknown", "unknown");
    private final String name;
    private final String type;

    CBCSymmetricCryptoAlgorithm(String algorithm, String type) {

        this.name = algorithm;
        this.type = type;

    }

    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return type;
    }
}
