package com.gow.test.demo;

import com.gow.algorithm.crypto.CBCSymmetricCrypto;
import com.gow.algorithm.crypto.CBCSymmetricCryptoAlgorithm;
import com.gow.algorithm.crypto.CryptoFactory;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author gow
 * @date 2021/7/1
 */
public class DecryptTest {

    public static void main(String[] args) {
        String key = "KK2s9TaAuTkI7OGl";
        String message = "MDAxOQ==";
        CBCSymmetricCrypto cbcSymmetricCrypto =
                CryptoFactory.createCBCSymmetricCrypto(CBCSymmetricCryptoAlgorithm.AES_CBC,
                        key.getBytes(StandardCharsets.UTF_8), 16, 16);

        byte[] decode = Base64.getDecoder().decode(message);
        byte[] decrypt = cbcSymmetricCrypto.decrypt(decode);
        String result = new String(decrypt);
        System.out.println(result);
    }
}
