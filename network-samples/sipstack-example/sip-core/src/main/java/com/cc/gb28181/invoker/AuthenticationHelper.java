/*
 * Conditions Of Use
 *
 * This software was developed by employees of the National Institute of
 * Standards and Technology (NIST), an agency of the Federal Government.
 * Pursuant to title 15 Untied States Code Section 105, works of NIST
 * employees are not subject to copyright protection in the United States
 * and are considered to be in the public domain.  As a result, a formal
 * license is not needed to use the software.
 *
 * This software is provided by NIST as a service and is expressly
 * provided "AS IS."  NIST MAKES NO WARRANTY OF ANY KIND, EXPRESS, IMPLIED
 * OR STATUTORY, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTY OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, NON-INFRINGEMENT
 * AND DATA ACCURACY.  NIST does not warrant or make any representations
 * regarding the use of the software or the results thereof, including but
 * not limited to the correctness, accuracy, reliability or usefulness of
 * the software.
 *
 * Permission to use this software is contingent upon your acceptance
 * of the terms of this agreement
 *
 * .
 *
 */
package com.cc.gb28181.invoker;

import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;

import javax.sip.address.URI;
import javax.sip.header.AuthorizationHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;

/**
 * Implements the HTTP digest authentication method server side functionality.
 * <p>
 * wcc 2022/5/24
 */
public class AuthenticationHelper {


    public static final String DEFAULT_ALGORITHM = "MD5";
    public static final String DEFAULT_SCHEME = "Digest";

    public AuthenticationHelper() {

    }

    /**
     * Generate the challenge string.
     *
     * @return a generated nonce.
     */
    private static String generateNonce() {
        // Get the time of day and run MD5 over it.
        Date date = new Date();
        long time = date.getTime();
        Random rand = new Random();
        long pad = rand.nextLong();
        String nonceString = (new Long(time)).toString() + (new Long(pad)).toString();
        // Convert the mdbytes array into a hex string.
        return DigestUtils.md5Hex(nonceString);
    }

    @SneakyThrows
    public static Response generateChallenge(HeaderFactory headerFactory, Response response, String realm) {
        WWWAuthenticateHeader proxyAuthenticate = headerFactory
                .createWWWAuthenticateHeader(DEFAULT_SCHEME);
        proxyAuthenticate.setParameter("realm", realm);
        proxyAuthenticate.setParameter("nonce", generateNonce());

        proxyAuthenticate.setParameter("opaque", "");
        proxyAuthenticate.setParameter("stale", "FALSE");
        proxyAuthenticate.setParameter("algorithm", DEFAULT_ALGORITHM);

//            proxyAuthenticate.setParameter("qop", "auth");
        response.setHeader(proxyAuthenticate);
        return response;
    }

    /**
     * Authenticate the inbound request.
     *
     * @param request        - the request to authenticate.
     * @param hashedPassword -- the MD5 hashed string of username:realm:plaintext password.
     * @return true if authentication succeded and false otherwise.
     */
    public static boolean doAuthenticateHashedPassword(Request request, String hashedPassword) {
        AuthorizationHeader authHeader = (AuthorizationHeader) request.getHeader(AuthorizationHeader.NAME);
        if (authHeader == null) return false;
        String realm = authHeader.getRealm();
        String username = authHeader.getUsername();

        if (username == null || realm == null) {
            return false;
        }

        String nonce = authHeader.getNonce();
        URI uri = authHeader.getURI();
        if (uri == null) {
            return false;
        }


        String A2 = request.getMethod().toUpperCase() + ":" + uri.toString();
        String HA1 = hashedPassword;


        String HA2 = DigestUtils.md5Hex(A2);

        String cnonce = authHeader.getCNonce();
        String KD = HA1 + ":" + nonce;
        if (cnonce != null) {
            KD += ":" + cnonce;
        }
        KD += ":" + HA2;
        String mdString = DigestUtils.md5Hex(KD);
        String response = authHeader.getResponse();


        return mdString.equals(response);
    }

    /**
     * Authenticate the inbound request given plain text password.
     *
     * @param request - the request to authenticate.
     * @param pass    -- the plain text password.
     * @return true if authentication succeded and false otherwise.
     */
    public static boolean doAuthenticatePlainTextPassword(Request request, String pass) {
        AuthorizationHeader authHeader = (AuthorizationHeader) request.getHeader(AuthorizationHeader.NAME);
        if (authHeader == null) return false;
        String realm = authHeader.getRealm();
        String username = authHeader.getUsername();

        if (username == null || realm == null || pass == null) {
            return false;
        }

        String nonce = authHeader.getNonce();
        URI uri = authHeader.getURI();
        if (uri == null) {
            return false;
        }
        // qop 保护质量 包含auth（默认的）和auth-int（增加了报文完整性检测）两种策略
        String qop = authHeader.getQop();

        // 客户端随机数，这是一个不透明的字符串值，由客户端提供，并且客户端和服务器都会使用，以避免用明文文本。
        // 这使得双方都可以查验对方的身份，并对消息的完整性提供一些保护
        String cNonce = authHeader.getCNonce();

        // nonce计数器，是一个16进制的数值，表示同一nonce下客户端发送出请求的数量
        int nc = authHeader.getNonceCount();
        String ncStr = new DecimalFormat("00000000").format(nc);
//        String ncStr = new DecimalFormat("00000000").format(Integer.parseInt(nc + "", 16));

        String A1 = username + ":" + realm + ":" + pass;
        String A2 = request.getMethod().toUpperCase() + ":" + uri.toString();
        String HA1 = DigestUtils.md5Hex(A1);
        String HA2 = DigestUtils.md5Hex(A2);
        String cnonce = authHeader.getCNonce();
        String KD = HA1 + ":" + nonce;

        if (qop != null && qop.equals("auth")) {
            if (nc != -1) {
                KD += ":" + ncStr;
            }
            if (cnonce != null) {
                KD += ":" + cnonce;
            }
            KD += ":" + qop;
        }
        KD += ":" + HA2;
        String mdString = DigestUtils.md5Hex(KD);
        String response = authHeader.getResponse();
        return mdString.equals(response);

    }


}
