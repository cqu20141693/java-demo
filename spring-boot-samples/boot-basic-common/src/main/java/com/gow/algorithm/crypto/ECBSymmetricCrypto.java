package com.gow.algorithm.crypto;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * @author gow
 * @date 2021/6/28 0028
 */
@Slf4j
public class ECBSymmetricCrypto implements Crypto {

    private byte[] seed;
    private final CryptoAlgorithm algorithm;

    public ECBSymmetricCrypto(byte[] seed, CryptoAlgorithm algorithm) {
        this.seed = seed;
        if (checkAlgorithm()) {
            throw new CryptoException("algorithm must be instanceof CBCSymmetricCryptoAlgorithm");
        }
        this.algorithm = algorithm;
    }

    @Override
    public byte[] encrypt(byte[] data) {
        try {
            Key key = generateKey(seed);
            Cipher cipher = Cipher.getInstance(algorithm.getType());
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            log.error("encrypt occur exception cause={} msg={}", e.getCause(), e.getMessage());
            return null;
        }
    }


    @Override
    public byte[] decrypt(byte[] data) {
        try {
            Key key = generateKey(seed);
            Cipher cipher = Cipher.getInstance(algorithm.getType());
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            log.error("decrypt occur exception cause={} msg={}", e.getCause(), e.getMessage());
            return null;
        }
    }

    private boolean checkAlgorithm() {
        if (algorithm == null) {
            log.error("encrypt algorithm is null");
            return true;
        }
        if (!(algorithm instanceof ECBSymmetricCryptoAlgorithm)) {
            log.error("algorithm must be instanceof ECBSymmetricCryptoAlgorithm,algorithm={}", algorithm.getName());
        }
        return false;
    }

    /**
     * 根据seed的 前keyLength 为作为Key
     *
     * @param seed
     * @return
     */
    public Key generateKey(byte[] seed) {
        return new SecretKeySpec(seed, algorithm.getName());
    }
}
