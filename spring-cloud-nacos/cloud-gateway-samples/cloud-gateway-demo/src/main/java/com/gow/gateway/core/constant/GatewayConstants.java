package com.gow.gateway.core.constant;

import com.gow.gateway.core.domain.auth.AuthDomain;

/**
 * @author gow
 * @date 2021/7/17 0017
 */
public class GatewayConstants {
    /**
     * auth domain header :{@link AuthDomain}
     */
    public static final String AUTH_DOMAIN_HEADER = "Auth-Domain";
    /**
     * Auth Token header : Auth-Handler parse token for authentication
     */
    public static final String AUTH_TOKEN_HEADER = "Auth-Token";
    /**
     * non auth header : Only generated by GlobalNonAuthFilter
     */
    public static final String NON_AUTH_HEADER = "Non_Auth";
}
