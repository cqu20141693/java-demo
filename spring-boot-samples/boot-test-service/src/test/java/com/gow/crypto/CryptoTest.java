package com.gow.crypto;

import static com.gow.constant.CommonConstants.DELIMITER;
import com.alibaba.fastjson.JSONObject;
import com.gow.algorithm.crypto.CBCSymmetricCrypto;
import com.gow.algorithm.crypto.CBCSymmetricCryptoAlgorithm;
import com.gow.algorithm.crypto.CryptoFactory;
import com.gow.algorithm.signature.HmacAlgorithm;
import com.gow.algorithm.signature.HmacSignature;
import com.gow.algorithm.signature.SignatureFactory;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author gow
 * @date 2021/8/23
 */
@Slf4j
public class CryptoTest {
    private final String sn = "cc-techDevice0001";
    private final String loginKey = "RTzQdVDPQEmEr5g2";
    private final String groupToken = "wfF1wpKDBkNRKZVv";
    private final String deviceKey = "FcLrkOhM20210622";
    private final String deviceToken = "rGrHslbhlEgKtGLN";

    private final CBCSymmetricCryptoAlgorithm aesCbc = CBCSymmetricCryptoAlgorithm.AES_CBC;
    private final CBCSymmetricCryptoAlgorithm sm4Cbc = CBCSymmetricCryptoAlgorithm.SM4_CBC;
    private final HmacSignature hmacSHA256Signature =
            SignatureFactory.createHmacSignatureWithoutKey(HmacAlgorithm.HmacSHA256);
    private final HmacSignature hmacSM3Signature =
            SignatureFactory.createHmacSignatureWithoutKey(HmacAlgorithm.HmacSM3);

    private String cryptoSecret = "L0yotOTXVBdKnWQu";

    @Test
    @DisplayName("create signature password")
    public void createPassword() {
        // groupSignature
        System.out.println(getSignature("G-HmacSHA256", groupToken, hmacSHA256Signature));
        System.out.println(getSignature("G-HmacSM3", groupToken, hmacSM3Signature));
        System.out.println(getSignature("GC-HmacSHA256", groupToken, hmacSHA256Signature));
        System.out.println(getSignature("GC-HmacSM3", groupToken, hmacSM3Signature));


        // deviceSignature
        System.out.println(getSignature("D-HmacSHA256", deviceToken, hmacSHA256Signature));
        System.out.println(getSignature("D-HmacSM3", deviceToken, hmacSM3Signature));
        System.out.println(getSignature("DC-HmacSHA256", deviceToken, hmacSHA256Signature));
        System.out.println(getSignature("DC-HmacSM3", deviceToken, hmacSM3Signature));

        System.out.println();
    }

    @Test
    @DisplayName("aes decode")
    public void aesDecode() {

        String cryptMsg = "2AjRmgt94p47ZTkMxklfFw==";

        CBCSymmetricCrypto cbcSymmetricCrypto =
                CryptoFactory.createCBCSymmetricCrypto(aesCbc,
                        cryptoSecret.getBytes(StandardCharsets.UTF_8), 16, 16);

        byte[] decode = Base64.getDecoder().decode(cryptMsg);
        byte[] decrypt = cbcSymmetricCrypto.decrypt(decode);
        log.info("msg={}", new String(decrypt));
    }

    @Test
    @DisplayName("sm4 decode")
    public void sm4Decode() {

        String cryptMsg = "McPp+q00wD8cswFbguO3Gw==";

        CBCSymmetricCrypto cbcSymmetricCrypto =
                CryptoFactory.createCBCSymmetricCrypto(sm4Cbc,
                        cryptoSecret.getBytes(StandardCharsets.UTF_8), 16, 16);

        byte[] decode = Base64.getDecoder().decode(cryptMsg);
        byte[] decrypt = cbcSymmetricCrypto.decrypt(decode);
        log.info("msg={}", new String(decrypt));

    }

    @Test
    @DisplayName("group decrypt welcome")
    public void groupDecryptWelcome() {
        // 解密welcome
        String welcomeAES =
                "wIIVz4T40s798OrvxvxmKC5MUw/G9npVddMviknzLUg3TjjO9JEzTKpOBHAn2yN67JFXc3ZVoarB8eK3gxu9k2rkSUWa"
                        + "+Az5qDQUznW1/1TO+UOjceA31+hR6K3BIlXhklbjBGoktUk9ZDfqgCKiBQ==";

        String welcomeSM4 =
                "TmwmlK/BnR99bpreu3ul1W5njAfOpPS5Yxf2"
                        + "+htKTaUhIIWfSqlbNMyxKbeSZM6kkSZ7u41QhIZCWuz5mny8kuzOJrONiDw9G9FuoBqPPwsjLM"
                        + "+nDpm9QnFXZjukMhaP2QFdr0NfwUTR28zAUKKlww==";

        extracted(groupToken, "cryptoSecret", welcomeAES, aesCbc, "aes-crypt-data-group");

        System.out.println();
        System.out.println("SM4");
        extracted(groupToken, "cryptoSecret", welcomeSM4, sm4Cbc, "sm4-crypt-data-group");

        System.out.println();
    }

    @Test
    @DisplayName("device decrypt welcome")
    public void deviceDecryptWelcome() {
        // 解密welcome
        String welcomeAES =
                "ydznXxnlqZlHkMd+NhtwbFd4Vhu/9iIAiyEDAe+r4pI622YWv7R+PDXvRB/8b7yE6eqw"
                        + "+rYth3kLLEvLNWFaJ9Dz5b6hmEHxI4Gt6dIRwLFjV745aPdbqwhO5nGxk9OFeks99RbsDFBebx/c+5S6aw==";

        String welcomeSM4 =
                "pf3u/T0i/mdJsR3cGvFbywUbPNQTLC6pfbA4MTzf+2bB4SDh9XNcfbhqIw5uzRwxQFXCVW/AyfdCJ/1PN9hGOVqNlZt8ixojA4"
                        + "/qT/nK4LSvZeJYv8VU33L1TFPUcbuKCwUw53NxFWHyNTQQnsrn8A==";

        extracted(deviceToken, "cryptoSecret", welcomeAES, aesCbc, "aes-crypt-data-device");

        System.out.println();
        System.out.println("SM4");
        extracted(deviceToken, "cryptoSecret", welcomeSM4, sm4Cbc, "sm4-crypt-data-device");

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
        log.info("cryptoSecret:{}", key);
        HashMap<String, Object> map = new HashMap<>();

        byte[] encrypt = getEncryptBytes("hex", cbc, msg, key, map);
        byte[] encryptBase64 = getEncryptBytes("base64", cbc, msg, key, map);

        String hex = Hex.toHexString(encrypt);
        log.info("hex:{}", hex);
        String encode = Base64.getEncoder().encodeToString(encryptBase64);
        log.info("base64:{}", encode);
        System.out.println();
    }

    private static byte[] getEncryptBytes(String encodeType, CBCSymmetricCryptoAlgorithm cbc, String msg, String key,
                                          HashMap<String, Object> map) {
        map.put(encodeType, msg);
        String message = JSONObject.toJSONString(map);
        CBCSymmetricCrypto cbcCrypto =
                CryptoFactory.createCBCSymmetricCrypto(cbc,
                        key.getBytes(StandardCharsets.UTF_8), 16, 16);
        return cbcCrypto.encrypt(message.getBytes(StandardCharsets.UTF_8));
    }

    private static String getSignature(String mode, String token, HmacSignature hmacSignature) {
        long timestamp = System.currentTimeMillis();
        String nonce = RandomStringUtils.randomAlphanumeric(16);
        String time = String.valueOf(timestamp);
        String origin = String.join(DELIMITER, token, nonce, time);
        hmacSignature.setKey(token.getBytes(StandardCharsets.UTF_8));
        byte[] bytes = hmacSignature.doSignature(origin.getBytes(StandardCharsets.UTF_8));
        String base64String = Base64.getEncoder().encodeToString(bytes);

        String join = String.join(DELIMITER, mode, base64String, nonce, time);
        return join;
    }
}
