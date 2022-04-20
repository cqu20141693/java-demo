package com.wujt.digest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 数字签名：
 * @author wujt
 */
public class DigestUtil {
    public enum Algorithm {

        MD2("MD2"),

        MD5("MD5"),

        SHA1("SHA-1"),

        SHA224("SHA-224"),

        SHA256("SHA-256"),

        SHA384("SHA-384"),

        SHA512("SHA-512");

        private final String value;

        Algorithm(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }

    }

    private final Algorithm algorithm;

    public DigestUtil(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public byte[] getRaw(byte[] data) {
        try {
            return MessageDigest.getInstance(algorithm.value()).digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }

    public byte[] getRaw(String data) {
        return getRaw(data.getBytes(StandardCharsets.UTF_8));
    }

    public String getHex(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : getRaw(data)) {
            sb.append(String.format("%02x", b & 0xFF));
        }
        return sb.toString();
    }

    public String getHex(String data) {
        return getHex(data.getBytes(StandardCharsets.UTF_8));
    }
}

