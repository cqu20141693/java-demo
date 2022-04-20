package com.gow.test.demo;

import com.gow.algorithm.crypto.CBCSymmetricCrypto;
import com.gow.algorithm.crypto.CBCSymmetricCryptoAlgorithm;
import com.gow.algorithm.crypto.CryptoFactory;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author gow
 * @date 2021/11/25
 */
public class Test {
    public static void main(String[] args) {
        CBCSymmetricCrypto cbcSymmetricCrypto =
                CryptoFactory.createCBCSymmetricCrypto(CBCSymmetricCryptoAlgorithm.SM4_CBC_V2,
                        "1234567890123456".getBytes(StandardCharsets.UTF_8), 16, 16);
        byte[] encrypt = cbcSymmetricCrypto.encrypt("java-data".getBytes(StandardCharsets.UTF_8));
        byte[] decrypt = cbcSymmetricCrypto.decrypt(encrypt);
        if ("java-data".equals(new String(decrypt))) {
            System.out.println("crypto value:" + Base64.getEncoder().encodeToString(encrypt));
        }
        encrypt = Base64.getDecoder().decode("ZKHpumvzPpnBK3gCW+4P9A==");
        decrypt = cbcSymmetricCrypto.decrypt(encrypt);
        if (!"go-data".equals(new String(decrypt))) {
            System.out.println("crypto failed");
        } else {
            System.out.println("cipherText:" + Base64.getEncoder().encodeToString(encrypt));
        }
    }
}
