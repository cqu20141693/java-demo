package com.wujt.digest;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

/**
 * Hmac数字签名： 签名之后不可逆转，只能后端用同样的签名的方式和key进行验证
 *
 * @author wujt
 */
public class HmacUtil {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public enum Algorithm {

        MD5("HmacMD5"),
        SM3("HmacSM3"),

        SHA1("HmacSHA1"),

        SHA224("HmacSHA224"),

        SHA256("HmacSHA256"),

        SHA384("HmacSHA384"),

        SHA512("HmacSHA512");

        private final String value;

        Algorithm(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }

    }

    private final Algorithm algorithm;

    public HmacUtil(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public Key generateKey(byte[] seed) {
        return new SecretKeySpec(seed, algorithm.value());
    }

    public Key generateKey(String seed) {
        return generateKey(seed.getBytes(StandardCharsets.UTF_8));
    }

    public Key generateKey() {
        try {
            return KeyGenerator.getInstance(algorithm.value()).generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getRaw(Key key, byte[] data) {
        try {
            Mac mac = Mac.getInstance(algorithm.value());
            mac.init(key);
            return mac.doFinal(data);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getRaw(Key key, String data) {
        return getRaw(key, data.getBytes(StandardCharsets.UTF_8));
    }

    public String getHex(Key key, byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : getRaw(key, data)) {
            sb.append(String.format("%02x", b & 0xFF));
        }
        return sb.toString();
    }

    public String getHex(Key key, String data) {
        return getHex(key, data.getBytes(StandardCharsets.UTF_8));
    }

}
