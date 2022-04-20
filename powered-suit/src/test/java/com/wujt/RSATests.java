package com.wujt;

import com.wujt.crypto.CryptoException;
import com.wujt.crypto.RSAUtil;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author wujt
 */
public class RSATests {
    private static final String TEXT = "Hello World!";

    @Test
    public void testCrypto() throws CryptoException {
        // 创建KeyPair： 可生产公钥和私钥
        KeyPair keyPair = RSAUtil.generateKeyPair(2048);
        // 公钥加密
        byte[] crypto = RSAUtil.encrypt(keyPair.getPublic(), TEXT.getBytes(StandardCharsets.UTF_8));
        // 私钥解密
        String text = new String(RSAUtil.decrypt(keyPair.getPrivate(), crypto), StandardCharsets.UTF_8);
        // 数据是否一致
        Assert.assertEquals(text, TEXT);
    }

    @Test
    public void testCryptoGenerateKey() throws CryptoException {
        // 创建KeyPair： 可生产公钥和私钥
        KeyPair keyPair = RSAUtil.generateKeyPair(2048);
        // 将加密的私钥根据PKCS#8规范解密还原
        PrivateKey privateKey = RSAUtil.generatePrivateKey(keyPair.getPrivate().getEncoded());
        // 将加密的公钥根据PKCS#8规范解密还原
        PublicKey publicKey = RSAUtil.generatePublicKey(keyPair.getPublic().getEncoded());
        //公钥加密
        byte[] crypto = RSAUtil.encrypt(publicKey, TEXT.getBytes(StandardCharsets.UTF_8));
        // 私钥解密
        String text = new String(RSAUtil.decrypt(privateKey, crypto), StandardCharsets.UTF_8);
        // 数据是否一致
        Assert.assertEquals(text, TEXT);
    }

    @Test
    public void testSignVerify() throws CryptoException {

        KeyPair keyPair = RSAUtil.generateKeyPair(2048);
        for (RSAUtil.SignatureAlgorithm signatureAlgorithm : RSAUtil.SignatureAlgorithm.values()) {
            // 私钥通过数据生成数据签名
            byte[] sign = RSAUtil.sign(signatureAlgorithm, keyPair.getPrivate(), TEXT.getBytes(StandardCharsets.UTF_8));
            // 公钥根据数据对签名进行验证
            boolean result = RSAUtil.verify(signatureAlgorithm, keyPair.getPublic(), TEXT.getBytes(StandardCharsets.UTF_8), sign);
            Assert.assertTrue(result);
        }
    }

}
