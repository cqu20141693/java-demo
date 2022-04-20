package com.wujt.com.wujt.shiro.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import lombok.Data;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * @author wujt
 */
@Component
@Data
@RefreshScope
public class JWTUtil {


    @Value("${shiro.cros:false}")
    private Boolean cros;

    @Value("${JWTParam.TOKEN_EXP}")
    private long tokenExp;

    @Value("${JWTParam.HY_REFRESH_TOKEN_EXP}")
    private long refreshTokenExp;

    @Value("${JWTParam.HY_COOKIE}")
    private String cookie;

    @Value("${JWTParam.HY_REFRESH_COOKIE}")
    private String refreshCookie;

    @Value("${JWTParam.BASE64_ENCODE_SECRET_KEY}")
    private String tokenSecret;

    @Value("${JWTParam.REFRESH_BASE64_ENCODE_SECRET_KEY}")
    private String refreshTokenSecret;

    private static final String LOCALHOST = "127.0.0.1";

    /**
     * 验证token是否正确
     */
    public boolean verify(String token, AccessInfo accessInfo, byte[] secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            Verification require = JWT.require(algorithm);
            JWTVerifier verifier = require
                    .withClaim("userName", accessInfo.getUsername())
                    .withClaim("userKey", accessInfo.getUserKey())
                    .build();

            DecodedJWT verify = verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }

    /**
     * 验证token是否正确
     */
    public boolean verifyOld(String token, AccessInfo accessInfo, byte[] secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            Verification require = JWT.require(algorithm);
            JWTVerifier verifier = require
                    .withSubject(accessInfo.getUsername())
                    .withJWTId(accessInfo.getUserKey())
                    .build();

            DecodedJWT verify = verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }

    public boolean verifyToken(String token, AccessInfo accessInfo) {
        return verify(token, accessInfo, tokenSecret.getBytes());
    }

    public boolean verifyRefreshToken(String token, AccessInfo accessInfo) {
        return verify(token, accessInfo, refreshTokenSecret.getBytes());
    }

    /**
     * 获得token中的自定义信息，无需secret解密也能获得
     */
    public static String getClaimFiled(String token, String filed) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(filed).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static AccessInfo getAccessInfo(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            String username = jwt.getClaim("username").asString();
            String userKey = jwt.getClaim("userKey").asString();
            return new AccessInfo(username, userKey);
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名
     */
    public String sign(AccessInfo accessInfo, String secret, long expireTime) {
        try {
            Date date = new Date(System.currentTimeMillis() + expireTime);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 附带username，nickname信息
            return JWT.create()
                    .withClaim("username", accessInfo.getUsername())
                    .withClaim("userKey", accessInfo.getUserKey())
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (JWTCreationException | UnsupportedEncodingException e) {
            return null;
        }
    }

    public String signToken(AccessInfo accessInfo) {
        return sign(accessInfo, this.tokenSecret, this.tokenExp);
    }

    public String signRefreshToken(AccessInfo accessInfo) {
        return sign(accessInfo, this.refreshTokenSecret, this.refreshTokenExp);
    }


    /**
     * 获取 token的签发时间
     */
    public static Date getIssuedAt(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getIssuedAt();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 验证 token是否过期
     */
    public static boolean isTokenExpired(String token) {
        Date now = Calendar.getInstance().getTime();
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt().before(now);
    }

    /**
     * 刷新 token的过期时间
     */
    public String refreshTokenExpired(String token, String secret) {
        DecodedJWT jwt = JWT.decode(token);
        Map<String, Claim> claims = jwt.getClaims();
        try {
            Date date = new Date(System.currentTimeMillis() + tokenExp);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTCreator.Builder builer = JWT.create().withExpiresAt(date);
            for (Map.Entry<String, Claim> entry : claims.entrySet()) {
                builer.withClaim(entry.getKey(), entry.getValue().asString());
            }
            return builer.sign(algorithm);
        } catch (JWTCreationException | UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 设置cookie
     *
     * @param request
     * @param response
     * @param accessInfo
     * @return token
     */
    public String setCookie(HttpServletRequest request, HttpServletResponse response, AccessInfo accessInfo) {
        String token = signToken(accessInfo);
        String refreshToken = signRefreshToken(accessInfo);
        Cookie cookie = new Cookie(getCookie(), token);
        cookie.setMaxAge((int) getTokenExp() / 1000);
        Cookie refreshCookie = new Cookie(getRefreshCookie(), refreshToken);
        refreshCookie.setMaxAge((int) (getRefreshTokenExp() / 1000));
        // cookie.setHttpOnly(true);暂时不用，前端js有多处直接获取
        String host = request.getServerName();

        if (!LOCALHOST.equals(host)) {
            cookie.setDomain(host.substring(host.indexOf(".") + 1));
            refreshCookie.setDomain(host.substring(host.indexOf(".") + 1));
        }
        refreshCookie.setPath("/");
        cookie.setPath("/");
        response.addCookie(cookie);
        response.addCookie(refreshCookie);
        return token;
    }

    /**
     * 从请求中获取cookie
     *
     * @param request
     * @param cookieName
     * @return
     */
    public static Cookie getCookie(ServletRequest request, String cookieName) {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);

        Cookie[] cookies = httpServletRequest.getCookies();
        return Optional.ofNullable(cookies).map(c -> {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie;
                }
            }
            return null;
        }).orElse(null);

    }

    /**
     * 生成16位随机盐
     */
    public static String generateSalt() {
        SecureRandomNumberGenerator secureRandom = new SecureRandomNumberGenerator();
        String hex = secureRandom.nextBytes(16).toHex();
        return hex;
    }

    public static void main(String[] args) throws InterruptedException {
        JWTUtil jwtUtil = new JWTUtil();
        try {
            Date date = new Date(System.currentTimeMillis());
            String secret = "VMrFFQNsBYM84hH+EysC6A==";
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 附带username，nickname信息

            String sign = JWT.create()
                    .withClaim("username", "admin")
                    .withClaim("userKey", " useKey")
                    .withExpiresAt(date)
                    .sign(algorithm);
            System.out.println(sign);
        } catch (JWTCreationException | UnsupportedEncodingException e) {

        }
    }

    public void clearCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie(getCookie(), "");
        cookie.setMaxAge(0);
        Cookie refreshCookie = new Cookie(getRefreshCookie(), "");
        refreshCookie.setMaxAge(0);
        // cookie.setHttpOnly(true);暂时不用，前端js有多处直接获取
        String host = request.getServerName();
        if (!LOCALHOST.equals(host)) {
            cookie.setDomain(host.substring(host.indexOf(".") + 1));
            refreshCookie.setDomain(host.substring(host.indexOf(".") + 1));
        }
        refreshCookie.setPath("/");
        cookie.setPath("/");
        response.addCookie(cookie);
        response.addCookie(refreshCookie);
    }
}
