package com.gow.algorithm.crypto;

/**
 * @author gow
 * @date 2021/6/28 0028
 */
public enum ECBSymmetricCryptoAlgorithm implements CryptoAlgorithm {


    AES_ECB("AES", "AES/ECB/PKCS5Padding"),
    DES_ECB("DES", "DES/ECB/PKCS5Padding"),

    UNKNOWN("unknown", "unknown");
    private final String name;
    private final String type;

    ECBSymmetricCryptoAlgorithm(String algorithm, String type) {

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
