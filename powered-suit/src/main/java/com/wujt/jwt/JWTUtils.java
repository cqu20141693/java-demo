package com.wujt.jwt;

import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;

/**
 * @author wujt
 */
public class JWTUtils {

    private static final String secret_key = "secret";
    private static final long max_age = 1000 * 60 * 60 * 24;

    public static void main(String[] args) {
        //获取算法HS256
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        Key signingKey = new SecretKeySpec(secret_key.getBytes(), signatureAlgorithm.getJcaName());

        LocalDateTime now = LocalDateTime.now();
        long epochMilli = now.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        Timestamp timestamp = new Timestamp(epochMilli);
        Timestamp expireAt = new Timestamp(epochMilli + max_age);
        HashMap<String, Object> map = new HashMap<>();
        map.put("role", 1);
        map.put("permission", "curd");

        String jws = Jwts.builder()
                .setIssuer("hyun") // 设置签发者
                .setAudience("console") // 设置接受者
                .setIssuedAt(timestamp) // 设置签发时间
                .setSubject("APP") // 设置目标
                .setExpiration(expireAt)  // 设置过期时间
                .setClaims(map) // 共有信息
                .signWith(signatureAlgorithm, signingKey)
                .compact();

        // 在header头中存在签名算法
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(jws);
            Claims body = claimsJws.getBody();
            assert body.getSubject().equals("Joe");
        } catch (JwtException e) {
            //don't trust the JWT!
        }
        // 为什么不用ras 加密的方式呢，因为该方法算力比较重，一般使用签名更好，除非特殊情况
    }
}
