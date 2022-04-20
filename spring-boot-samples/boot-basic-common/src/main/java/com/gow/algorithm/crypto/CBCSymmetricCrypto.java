package com.gow.algorithm.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gow
 * @date 2021/6/28 0028
 */
@Slf4j
public class CBCSymmetricCrypto implements Crypto {

    private byte[] seed;
    private final CryptoAlgorithm algorithm;
    private final int default_length = 16;
    /**
     * CBC key 长度
     */
    private final int keyLength;
    /**
     * CBC iv 长度
     */
    private final int ivLength;

    public CBCSymmetricCrypto(byte[] seed, CryptoAlgorithm algorithm) {
        assert seed.length < default_length : "seed length must be more than " + default_length;
        this.seed = seed;
        this.keyLength = default_length;
        this.ivLength = default_length;
        if (checkAlgorithm(algorithm)) {
            throw new CryptoException("algorithm must be instanceof CBCSymmetricCryptoAlgorithm");
        }
        this.algorithm = algorithm;
    }

    public CBCSymmetricCrypto(byte[] seed, int keyLength, int ivLength, CryptoAlgorithm algorithm) {
        int max = Math.max(keyLength, ivLength);
        assert seed.length <= max : "seed length must be more than " + max;
        this.seed = seed;
        this.keyLength = keyLength;
        this.ivLength = ivLength;
        if (checkAlgorithm(algorithm)) {
            throw new CryptoException("algorithm must be instanceof CBCSymmetricCryptoAlgorithm");
        }
        this.algorithm = algorithm;
    }

    @Override
    public byte[] encrypt(byte[] data) {
        try {
            Key key = generateKey(seed);
            AlgorithmParameterSpec iv = generateIv(seed);
            Cipher cipher = Cipher.getInstance(algorithm.getType());
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            return cipher.doFinal(data);

        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            log.error("encrypt occur exception cause={} msg={}", e.getCause(), e.getMessage());
            return null;
        }
    }


    @Override
    public byte[] decrypt(byte[] data) {
        try {
            Key key = generateKey(seed);
            AlgorithmParameterSpec iv = generateIv(seed);
            Cipher cipher = Cipher.getInstance(algorithm.getType());
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            return cipher.doFinal(data);

        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            log.error("decrypt occur exception cause={} msg={}", e.getCause(), e.getMessage());
            return null;
        }
    }

    private boolean checkAlgorithm(CryptoAlgorithm algorithm) {
        if (algorithm == null) {
            log.error("encrypt algorithm is null");
            return true;
        }
        if (!(algorithm instanceof CBCSymmetricCryptoAlgorithm)) {
            log.error("algorithm must be instanceof CBCSymmetricCryptoAlgorithm,algorithm={}",
                    this.algorithm.getName());
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
        return new SecretKeySpec(Arrays.copyOf(seed, keyLength), algorithm.getName());
    }

    /**
     * 根据seed 的前ivLength 位作为IV
     *
     * @param seed
     * @return
     */
    public AlgorithmParameterSpec generateIv(byte[] seed) {
        return new IvParameterSpec(Arrays.copyOf(seed, ivLength));
    }
}
