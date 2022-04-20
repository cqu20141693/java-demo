package com.gow.algorithm.crypto;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Optional;

/**
 * RSA 使用流程
 * 1. 生成公钥和私钥，私钥保留在本地，共钥发放给使用者
 * 2. 以使用方发送数据为例：
 * 使用方使用PublicKey 对数据进行加密，并对数据进行签名发送
 * 生成方首先时对数据进行解密，然后再通过签名 验证是否被改动过
 *
 * @author gow
 * @date 2021/6/29 0029
 */
@Slf4j
public class RSAAsymmetricCrypto implements Crypto {

    private static final String ALGORITHM = "RSA";
    private final int keySize;
    private final KeySpecEncodeEnum keySpecEncode;
    private final KeyPair keyPair;

    public RSAAsymmetricCrypto(int keySize, KeySpecEncodeEnum keySpecEncode) {
        this.keySize = keySize;
        this.keySpecEncode = keySpecEncode;
        keyPair = generateKeyPair(keySize);
    }

    public enum KeySpecEncodeEnum {

        COMMON("common") {
            @Override
            PrivateKey generatePrivateKey(KeyPair keyPair) {

                return Optional.ofNullable(keyPair).map(value -> keyPair.getPrivate()).orElse(null);
            }

            @Override
            PublicKey generatePublicKey(KeyPair keyPair) {
                return Optional.ofNullable(keyPair).map(value -> keyPair.getPublic()).orElse(null);
            }
        },
        PKCS8("PKCS8") {
            @Override
            PrivateKey generatePrivateKey(KeyPair keyPair) {
                try {
                    PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
                    KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
                    return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
                } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                    log.info("generatePrivateKey occur exception,cause={},msg={}", e.getCause(), e.getMessage());
                    return null;
                }
            }

            @Override
            PublicKey generatePublicKey(KeyPair keyPair) {
                try {
                    X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
                    KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
                    return keyFactory.generatePublic(x509EncodedKeySpec);
                } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                    log.info("generatePublicKey occur exception,cause={},msg={}", e.getCause(), e.getMessage());
                    return null;
                }
            }
        };

        KeySpecEncodeEnum(String name) {
            this.name = name;
        }

        private String name;

        abstract PrivateKey generatePrivateKey(KeyPair keyPair);

        abstract PublicKey generatePublicKey(KeyPair keyPair);

    }

    @Override
    public byte[] encrypt(byte[] data) {
        try {
            PublicKey publicKey = keySpecEncode.generatePublicKey(keyPair);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            log.info("encrypt occur exception,cause={},msg={}", e.getCause(), e.getMessage());
            return null;
        }
    }

    @Override
    public byte[] decrypt(byte[] data) {
        try {
            PrivateKey privateKey = keySpecEncode.generatePrivateKey(keyPair);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            log.info("decrypt occur exception,cause={},msg={}", e.getCause(), e.getMessage());
            return null;
        }
    }

    public byte[] encryptByPrivateKey(byte[] data) {
        try {
            PrivateKey privateKey = keySpecEncode.generatePrivateKey(keyPair);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            log.info("encrypt occur exception,cause={},msg={}", e.getCause(), e.getMessage());
            return null;
        }
    }

    public byte[] decryptByPublicKey(byte[] data) {
        try {
            PublicKey publicKey = keySpecEncode.generatePublicKey(keyPair);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            log.info("decrypt occur exception,cause={},msg={}", e.getCause(), e.getMessage());
            return null;
        }
    }

    /**
     * 生成秘钥对
     * 可以加密的数据长度为：keysize / 8-11
     */
    public static KeyPair generateKeyPair(int keySize) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
            keyPairGenerator.initialize(keySize);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            log.info("generateKeyPair occur exception,cause={},msg={}", e.getCause(), e.getMessage());
            return null;
        }
    }

    public enum SignatureAlgorithm {

        MD2("MD2withRSA"),

        MD5("MD5withRSA"),

        SHA1("SHA1withRSA"),

        SHA256("SHA256withRSA"),

        SHA384("SHA384withRSA"),

        SHA512("SHA512withRSA");

        private final String value;

        SignatureAlgorithm(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }

    }

    /**
     * 私钥签名
     */
    public static byte[] sign(SignatureAlgorithm signatureAlgorithm, PrivateKey privateKey, byte[] data) throws CryptoException {
        try {
            Signature signature = Signature.getInstance(signatureAlgorithm.value());
            signature.initSign(privateKey);
            signature.update(data);
            return signature.sign();
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            log.info("sign occur exception,cause={},msg={}", e.getCause(), e.getMessage());
            return null;
        }
    }

    /**
     * 公钥校验
     */
    public static boolean verify(SignatureAlgorithm signatureAlgorithm, PublicKey publicKey, byte[] data, byte[] sign) throws CryptoException {
        try {
            Signature signature = Signature.getInstance(signatureAlgorithm.value());
            signature.initVerify(publicKey);
            signature.update(data);
            return signature.verify(sign);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            log.info("verify occur exception,cause={},msg={}", e.getCause(), e.getMessage());
            return false;
        }
    }


}
