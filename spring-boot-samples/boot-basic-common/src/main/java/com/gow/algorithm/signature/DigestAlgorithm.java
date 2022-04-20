package com.gow.algorithm.signature;

/**
 * @author gow
 * @date 2021/6/28 0028
 */
public enum DigestAlgorithm implements SignatureAlgorithm {

    MD5("MD5"),
    SHA1("SHA-1"),
    UNKNOWN("unknown");
    private final String name;

    DigestAlgorithm(String algorithm) {
        this.name = algorithm;
    }

    public String getName() {
        return name;
    }

}
