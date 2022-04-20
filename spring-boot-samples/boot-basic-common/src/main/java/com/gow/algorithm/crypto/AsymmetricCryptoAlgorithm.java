package com.gow.algorithm.crypto;

import static com.gow.algorithm.crypto.RSAAsymmetricCrypto.KeySpecEncodeEnum.COMMON;
import static com.gow.algorithm.crypto.RSAAsymmetricCrypto.KeySpecEncodeEnum.PKCS8;

/**
 * @author gow
 * @date 2021/6/28 0028
 */
public enum AsymmetricCryptoAlgorithm implements CryptoAlgorithm {


    RSA("RSA", COMMON),
    RSA_PKCS8("RSA", PKCS8),

    UNKNOWN("unknown", null);
    private final String name;

    public RSAAsymmetricCrypto.KeySpecEncodeEnum getKeySpecEncode() {
        return keySpecEncode;
    }

    private RSAAsymmetricCrypto.KeySpecEncodeEnum keySpecEncode;

    AsymmetricCryptoAlgorithm(String algorithm, RSAAsymmetricCrypto.KeySpecEncodeEnum keySpecEncode) {
        this.name = algorithm;
        this.keySpecEncode = keySpecEncode;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return keySpecEncode.name();
    }
}
