package com.gow.algorithm.signature;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author gow
 * @date 2021/6/28 0028
 */
@Slf4j
public class HmacSignature implements Signature {
    /**
     * Hmac key
     */
    private byte[] key;

    private SignatureAlgorithm algorithm;

    public HmacSignature(SignatureAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public HmacSignature(byte[] key, SignatureAlgorithm algorithm) {
        this.key = key;
        this.algorithm = algorithm;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    @Override
    public byte[] doSignature(byte[] data) {
        try {
            if (algorithm == null || key == null) {
                log.error("doSignature algorithm={} or key={} is null", algorithm, key);
                return null;
            }
            if (!(algorithm instanceof HmacAlgorithm)) {
                log.error("algorithm must be instanceof HmacSignature ,algorithm={}", algorithm.getName());
                return null;
            }
            SecretKeySpec keySpec = new SecretKeySpec(key, algorithm.getName());
            Mac mac = Mac.getInstance(algorithm.getName());
            mac.init(keySpec);
            return mac.doFinal(data);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("signature occur exception cause={} msg={}", e.getCause(), e.getMessage());
            return null;
        }
    }

}
