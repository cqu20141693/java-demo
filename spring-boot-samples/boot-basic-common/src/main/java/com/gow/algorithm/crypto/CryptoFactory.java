package com.gow.algorithm.crypto;

import java.security.Security;
import java.util.Optional;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * @author gow
 * @date 2021/6/28 0028
 */
public class CryptoFactory {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static CBCSymmetricCrypto createCBCSymmetricCrypto(CBCSymmetricCryptoAlgorithm cryptoAlgorithm, byte[] seed,
                                                              int keyLength, int ivLength) {
        return Optional.ofNullable(cryptoAlgorithm)
                .map(algorithm -> new CBCSymmetricCrypto(seed, keyLength, ivLength, cryptoAlgorithm)).orElse(null);
    }

    public static ECBSymmetricCrypto createECBSymmetricCrypto(ECBSymmetricCryptoAlgorithm cryptoAlgorithm,
                                                              byte[] seed) {
        return Optional.ofNullable(cryptoAlgorithm).map(algorithm -> new ECBSymmetricCrypto(seed, cryptoAlgorithm))
                .orElse(null);
    }

    public static RSAAsymmetricCrypto createRSAAsymmetricCrypto(AsymmetricCryptoAlgorithm cryptoAlgorithm,
                                                                int keySize) {
        return Optional.ofNullable(cryptoAlgorithm)
                .map(algorithm -> new RSAAsymmetricCrypto(keySize, cryptoAlgorithm.getKeySpecEncode())).orElse(null);
    }

    public static Crypto createDefault(CryptoAlgorithm cryptoAlgorithm, byte[] seed) {
        return Optional.ofNullable(cryptoAlgorithm).map(algorithm -> {
            if (algorithm instanceof CBCSymmetricCryptoAlgorithm) {
                return new CBCSymmetricCrypto(seed, cryptoAlgorithm);
            } else if (algorithm instanceof ECBSymmetricCryptoAlgorithm) {
                return new ECBSymmetricCrypto(seed, cryptoAlgorithm);
            } else if (algorithm instanceof AsymmetricCryptoAlgorithm) {
                AsymmetricCryptoAlgorithm asymmetricCryptoAlgorithm = (AsymmetricCryptoAlgorithm) cryptoAlgorithm;
                return new RSAAsymmetricCrypto(1024, asymmetricCryptoAlgorithm.getKeySpecEncode());
            }
            return null;
        }).orElse(null);
    }
}
