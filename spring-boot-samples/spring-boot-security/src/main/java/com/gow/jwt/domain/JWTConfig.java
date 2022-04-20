package com.gow.jwt.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author gow
 * @date 2021/8/2 0002
 */
@Configuration
@ConfigurationProperties("gow.jwt")
@Data
public class JWTConfig {

    private String token = "AUTH_TOKEN";
    private String tokenSecretBase64 = "VMrFFQNsBYM84hH+EysC6A==";
    private long tokenExp = 24 * 3600 * 1000;

    private String refreshToken = "REFRESH_AUTH_TOKEN";
    private String refreshTokenSecretBase64 = "lfrSSQNs9hI84hH+msOC6A=0";
    private long refreshTokenExp = 7 * 24 * 3600 * 1000;
    private long maxRefreshTokenExp = 15 * 24 * 3600 * 1000;

    private Boolean httpOnly = false;


    private Boolean security = false;

    private String bearer = "agriculture";

    private long delayTime = 60 * 1000;
}
