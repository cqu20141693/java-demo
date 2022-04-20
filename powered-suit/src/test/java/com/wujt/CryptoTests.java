package com.wujt;

import com.wujt.crypto.CryptoException;
import com.wujt.crypto.CryptoUtil;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @author wujt
 */
public class CryptoTests {
    private static final byte[] KEY_RAW = "58e00488-2014-4947-ab29-40cfa1f0d692".getBytes(StandardCharsets.UTF_8);
    private static final String[] DATAS = new String[]{
            "HelloWorld",
            "TakWolf",
            "Google",
            "今天的风儿有点喧嚣",
            "おはよう",
            "http://takwolf.com"
    };

    @Test
    public void testAES() throws CryptoException {
        // AES加密工具创建
        CryptoUtil aes = new CryptoUtil(CryptoUtil.Algorithm.AES);
        // 创建 AES  Key
        Key key = aes.generateKey(KEY_RAW);
        // 创建 IV
        AlgorithmParameterSpec iv = aes.generateIv(KEY_RAW);
        for (String data : DATAS) {
            // 对数据利用Key 和IV 加密
            byte[] encrypt = aes.encrypt(key, iv, data.getBytes(StandardCharsets.UTF_8));
            //// 利用Key 和IV 对加密数据解密得到数据
            byte[] decrypt = aes.decrypt(key, iv, encrypt);
            Assert.assertEquals(data, new String(decrypt, StandardCharsets.UTF_8));
        }
    }

    @Test
    public void testDESede() throws CryptoException {
        CryptoUtil desede = new CryptoUtil(CryptoUtil.Algorithm.DESEDE);
        Key key = desede.generateKey(KEY_RAW);
        AlgorithmParameterSpec iv = desede.generateIv(KEY_RAW);
        for (String data : DATAS) {
            byte[] encrypt = desede.encrypt(key, iv, data.getBytes(StandardCharsets.UTF_8));
            byte[] decrypt = desede.decrypt(key, iv, encrypt);
            Assert.assertEquals(data, new String(decrypt, StandardCharsets.UTF_8));
        }
    }

    @Test
    public void testCrypto() throws CryptoException {
        for (CryptoUtil.Algorithm algorithm : CryptoUtil.Algorithm.values()) {
            CryptoUtil crypto = new CryptoUtil(algorithm);
            Key key = crypto.generateKey(KEY_RAW);
            AlgorithmParameterSpec iv = crypto.generateIv(KEY_RAW);
            for (String data : DATAS) {
                byte[] encrypt = crypto.encrypt(key, iv, data.getBytes(StandardCharsets.UTF_8));
                byte[] decrypt = crypto.decrypt(key, iv, encrypt);
                Assert.assertEquals(data, new String(decrypt, StandardCharsets.UTF_8));
            }
        }
    }
}
