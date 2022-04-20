package com.gow.algorithm.signature;

/**
 * @author gow
 * @date 2021/6/28 0028
 */
public enum HmacAlgorithm implements SignatureAlgorithm {

    HmacSHA1("HmacSHA1"),
    HmacSHA256("HmacSHA256"),
    HmacSM3("HmacSM3");
    private final String name;

    HmacAlgorithm(String algorithm) {
        this.name = algorithm;
    }

    public String getName() {
        return name;
    }

}
