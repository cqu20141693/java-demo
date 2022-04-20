package com.gow.gateway.core.domain.auth.token;

import static com.gow.gateway.core.domain.auth.token.TokenType.plaintext;
import com.gow.algorithm.signature.HmacAlgorithm;
import com.gow.algorithm.signature.Signature;
import com.gow.algorithm.signature.SignatureFactory;
import java.nio.charset.StandardCharsets;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author gow
 * @date 2021/7/17 0017
 */
public abstract class AuthToken {

    protected TokenType tokenType = plaintext;


    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }


    protected String getRealToken(String key, String token) {
        assert tokenType != null : "tokenType can not be null";

        Signature hmacSignature;
        byte[] bytes;
        String signPassword;
        switch (tokenType) {
            case plaintext:
                signPassword = token;
                break;
            case signature:
                hmacSignature =
                        SignatureFactory
                                .createHmacSignature(HmacAlgorithm.HmacSHA1, token.getBytes(StandardCharsets.UTF_8));
                bytes = hmacSignature.doSignature(key.getBytes(StandardCharsets.UTF_8));
                signPassword = new String(bytes);
                break;
            case one_time_signature:
                long current = System.currentTimeMillis();
                String nonce = RandomStringUtils.randomAlphanumeric(8);
                String compose = key + ":" + nonce + ":" + current;
                hmacSignature =
                        SignatureFactory
                                .createHmacSignature(HmacAlgorithm.HmacSHA1, token.getBytes(StandardCharsets.UTF_8));
                bytes = hmacSignature.doSignature(compose.getBytes(StandardCharsets.UTF_8));
                signPassword = new String(bytes) + ":" + nonce + ":" + current;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + tokenType);
        }
        return signPassword;
    }
}
