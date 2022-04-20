package com.gow.algorithm.signature;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author gow
 * @date 2021/6/28 0028
 */
public class SignatureFactory {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private final static Map<String, HmacAlgorithm> INNERHmac;
    private final static Map<String, DigestAlgorithm> INNERDIGEST;

    static {
        INNERHmac = new HashMap<>();
        INNERDIGEST = new HashMap<>();
        for (HmacAlgorithm hmacAlgorithm : HmacAlgorithm.values()) {
            INNERHmac.put(hmacAlgorithm.getName(), hmacAlgorithm);
        }
        for (DigestAlgorithm digestAlgorithm : DigestAlgorithm.values()) {
            INNERDIGEST.put(digestAlgorithm.getName(), digestAlgorithm);
        }
    }

    /**
     * create Hmac signature
     *
     * @param signatureAlgorithm
     * @param key
     * @return
     */
    public static Signature createHmacSignature(SignatureAlgorithm signatureAlgorithm, byte[] key) {
        HmacAlgorithm hmacAlgorithm = INNERHmac.get(signatureAlgorithm.getName());
        return Optional.ofNullable(hmacAlgorithm).map(algorithm ->
                new HmacSignature(key, hmacAlgorithm)).orElse(null);
    }

    /**
     * create Hmac signature without key
     *
     * @param signatureAlgorithm
     * @return
     */
    public static HmacSignature createHmacSignatureWithoutKey(SignatureAlgorithm signatureAlgorithm) {
        HmacAlgorithm hmacAlgorithm = INNERHmac.get(signatureAlgorithm.getName());
        return new HmacSignature(hmacAlgorithm);
    }

    /**
     * create Hmac signature without key
     *
     * @param signatureAlgorithm
     * @return
     */
    public static DigestSignature createDigestSignature(SignatureAlgorithm signatureAlgorithm) {
        DigestAlgorithm digestAlgorithm = INNERDIGEST.get(signatureAlgorithm.getName());
        return new DigestSignature(digestAlgorithm);
    }

}
