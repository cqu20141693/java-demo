package com.gow.test.demo;

import static com.gow.constant.CommonConstants.DELIMITER;
import com.gow.algorithm.signature.HmacAlgorithm;
import com.gow.algorithm.signature.HmacSignature;
import com.gow.algorithm.signature.SignatureFactory;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author gow
 * @date 2021/6/29
 */
public class SignatureTest {

    public static void main(String[] args) {
        String sn = "gateway_Sn";
        String loginKey = "yKbb7TlRuOMDOAqp";
        String groupToken = "2fw6oC2eVtDKraks";
        String deviceKey = "jzpqdAwU20211020";
        String deviceToken = "UlWAEGZqeSmAHsSi";

        HmacSignature hmacSHA256Signature = SignatureFactory.createHmacSignatureWithoutKey(HmacAlgorithm.HmacSHA256);

        HmacSignature hmacSM3Signature = SignatureFactory.createHmacSignatureWithoutKey(HmacAlgorithm.HmacSM3);

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
