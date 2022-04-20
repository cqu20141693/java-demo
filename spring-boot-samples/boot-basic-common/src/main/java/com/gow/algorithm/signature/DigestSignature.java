package com.gow.algorithm.signature;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author gow
 * @date 2021/6/28 0028
 */
@Slf4j
public class DigestSignature implements Signature {

    private SignatureAlgorithm algorithm;

    public DigestSignature(SignatureAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public byte[] doSignature(byte[] data) {
        try {

            if (algorithm == null) {
                log.error("doSignature algorithm is null");
                return null;
            }
            if (!(algorithm instanceof DigestAlgorithm)) {
                log.error("algorithm must be instanceof DigestSignature ,algorithm={}", algorithm.getName());
                return null;
            }
            return MessageDigest.getInstance(algorithm.getName()).digest(data);

        } catch (NoSuchAlgorithmException e) {
            log.error("signature occur exception cause={} msg={}", e.getCause(), e.getMessage());
            return null;
        }
    }

}
