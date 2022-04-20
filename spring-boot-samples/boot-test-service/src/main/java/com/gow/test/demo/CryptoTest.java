package com.gow.test.demo;

import com.alibaba.fastjson.JSONObject;
import com.gow.algorithm.crypto.CBCSymmetricCrypto;
import com.gow.algorithm.crypto.CBCSymmetricCryptoAlgorithm;
import com.gow.algorithm.crypto.CryptoFactory;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import org.bouncycastle.util.encoders.Hex;

/**
 * @author gow
 * @date 2021/7/1
 */
public class CryptoTest {


    public static void main(String[] args) {
        String sn = "cc-techDevice0001";
        String loginKey = "RTzQdVDPQEmEr5g2";
        String groupToken = "wfF1wpKDBkNRKZVv";
        String deviceKey = "FcLrkOhM20210622";
        String deviceToken = "rGrHslbhlEgKtGLN";

        String cryptKey = "cryptoSecret";

        // 解密welcome
        String welcomeAES =
                "wIIVz4T40s798OrvxvxmKC5MUw/G9npVddMviknzLUg3TjjO9JEzTKpOBHAn2yN67JFXc3ZVoarB8eK3gxu9k2rkSUWa"
                        + "+Az5qDQUznW1/1TO+UOjceA31+hR6K3BIlXhklbjBGoktUk9ZDfqgCKiBQ==";

        String welcomeSM4 =
                "pf3u/T0i/mdJsR3cGvFbywUbPNQTLC6pfbA4MTzf+2aOh2vfw6UCUPHnwTVC1+JAWEQbgdLb40NdEGpa18/SO4Q3tN"
                        + "+aglOo71QVIeUoKDUsGhYzaMnb7tDi0/vqVM+lLUUMhZ8hYteaJ7nxXFG8JQ==";
        CBCSymmetricCryptoAlgorithm aesCbc = CBCSymmetricCryptoAlgorithm.AES_CBC;
        CBCSymmetricCryptoAlgorithm sm4Cbc = CBCSymmetricCryptoAlgorithm.SM4_CBC;
        extracted(groupToken, cryptKey, welcomeAES, aesCbc, "aes-crypt-data-test-last");

        System.out.println();
        extracted(groupToken, cryptKey, welcomeSM4, sm4Cbc, "sm4-crypt-data-test-last");

        System.out.println();
    }

    private static void extracted(String groupToken, String cryptKey, String welcome,
                                  CBCSymmetricCryptoAlgorithm cbc, String msg) {
        CBCSymmetricCrypto cbcSymmetricCrypto =
                CryptoFactory.createCBCSymmetricCrypto(cbc,
                        groupToken.getBytes(StandardCharsets.UTF_8), 16, 16);

        byte[] decode = Base64.getDecoder().decode(welcome);
        byte[] decrypt = cbcSymmetricCrypto.decrypt(decode);
        String result = new String(decrypt);
        System.out.println(result);

        JSONObject jsonObject = JSONObject.parseObject(result, JSONObject.class);

        String key = jsonObject.getString(cryptKey);
        System.out.println("cryptoSecret:" + key);
        HashMap<String, Object> map = new HashMap<>();

        byte[] encrypt = getEncrytBytes("hex", cbc, msg, key, map);
        byte[] encryptBase64 = getEncrytBytes("base64", cbc, msg, key, map);

        String hex = Hex.toHexString(encrypt);
        System.out.println("hex:" + hex);
        String encode = Base64.getEncoder().encodeToString(encryptBase64);
        System.out.println("base64:" + encode);
        System.out.println();
    }

    private static byte[] getEncrytBytes(String encodeType, CBCSymmetricCryptoAlgorithm cbc, String msg, String key,
                                         HashMap<String, Object> map) {
        map.put(encodeType, msg);
        String message = JSONObject.toJSONString(map);
        CBCSymmetricCrypto cbcCrypto =
                CryptoFactory.createCBCSymmetricCrypto(cbc,
                        key.getBytes(StandardCharsets.UTF_8), 16, 16);
        return cbcCrypto.encrypt(message.getBytes(StandardCharsets.UTF_8));
    }
}
