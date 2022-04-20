package com.gow.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gow.jwt.domain.AccessInfo;
import com.gow.jwt.domain.JWTConfig;
import com.gow.jwt.domain.PlatformEnum;
import com.gow.jwt.domain.Tokens;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


/**
 * @author gow
 * @date 2021/8/2 0002
 */
@Component
@Data
public class JWTUtil {
    private static final String LOCALHOST = "127.0.0.1";

    private final static String extendKey = "extend";
    private final static String beforeKey = "before";
    private final static String platformKey = "platform";

    /**
     * 验证token是否正确
     */
    private DecodedJWT verify(String token, String secret) {
        try {
            byte[] secretBytes = DatatypeConverter.parseBase64Binary(secret);
            Algorithm algorithm = Algorithm.HMAC256(secretBytes);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            // 校验算法，校验过期时间，校验签名
            DecodedJWT jwt = verifier.verify(token);
            Date date = jwt.getClaim(beforeKey).asDate();
            Optional.ofNullable(date).ifPresent(this::shouldBefore);
            return jwt;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private void shouldBefore(Date date) {
        Date today = new Date();
        long epochMilli = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        // truncate millis
        today.setTime((long) Math.floor((epochMilli / 1000) * 1000));
        if (date != null && today.after(date)) {
            throw new TokenExpiredException(
                    String.format("The RefreshToken exceeded the maximum validity period on %s.", date));
        }
    }


    public AccessInfo verifyToken(String token, JWTConfig config) {
        String bearer = config.getBearer();
        DecodedJWT decodedJWT = null;
        if (!StringUtils.hasText(bearer)) {
            decodedJWT = verify(token, config.getTokenSecretBase64());
        } else {
            if (token.startsWith(bearer)) {
                decodedJWT = verify(token.substring(bearer.length()), config.getTokenSecretBase64());
            }
        }

        return Optional.ofNullable(decodedJWT).map(JWTUtil::getAccessInfo).orElse(null);
    }

    public AccessInfo verifyRefreshToken(String token, JWTConfig config) {
        String bearer = config.getBearer();
        DecodedJWT decodedJWT = null;
        if (!StringUtils.hasText(bearer)) {
            decodedJWT = verify(token, config.getRefreshTokenSecretBase64());

        } else {
            if (token.startsWith(bearer)) {
                decodedJWT = verify(token.substring(bearer.length()), config.getRefreshTokenSecretBase64());
            }
        }
        return Optional.ofNullable(decodedJWT).map(JWTUtil::getAccessInfo).orElse(null);
    }


    private static DecodedJWT decode(String token, JWTConfig config) {
        return StringUtils.hasText(config.getBearer()) ? JWT.decode(token) :
                JWT.decode(token.substring(config.getBearer().length()));
    }

    public static AccessInfo getAccessInfo(String token, JWTConfig config) {
        try {
            DecodedJWT jwt = decode(token, config);
            return getAccessInfo(jwt);
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    private static AccessInfo getAccessInfo(DecodedJWT jwt) {
        String userKey = jwt.getId();
        String extend = jwt.getClaim(extendKey).asString();
        Date beforeDate = jwt.getClaim(beforeKey).asDate();
        PlatformEnum platform = PlatformEnum.createByName(jwt.getClaim(platformKey).asString());
        return new AccessInfo(userKey, extend, platform, beforeDate);
    }

    /**
     * 生成token签名
     */
    public String signToken(AccessInfo accessInfo, JWTConfig config) {
        try {
            byte[] secretBytes = DatatypeConverter.parseBase64Binary(config.getTokenSecretBase64());
            // 前端发送请求时cookie并为过期，携带了cookie,此时并发请求出现某些请求通过，后续请求失败
            // 后端生成cookie时的过期时间应该大于cookie的最大年龄，保证前端携带未过期的cookie能够通过，
            // 默认值1分钟
            Date date = new Date(System.currentTimeMillis() + config.getTokenExp() + config.getDelayTime());
            Algorithm algorithm = Algorithm.HMAC256(secretBytes);
            // 附带username，nickname信息
            String sign = JWT.create()
                    .withJWTId(accessInfo.getUserKey())
                    .withExpiresAt(date)
                    .withClaim(extendKey, accessInfo.getExtend())
                    .withClaim(platformKey, accessInfo.getPlatform().name())
                    .sign(algorithm);
            String bearer = config.getBearer();
            return StringUtils.hasText(bearer) ? bearer + sign : sign;

        } catch (JWTCreationException e) {
            return null;
        }
    }

    public String signRefreshToken(AccessInfo accessInfo, JWTConfig config) {
        try {
            byte[] secretBytes = DatatypeConverter.parseBase64Binary(config.getRefreshTokenSecretBase64());
            long currentTimeMillis = System.currentTimeMillis();
            Date date = new Date(currentTimeMillis + config.getRefreshTokenExp() + config.getDelayTime());
            Date beforeDate = Optional.ofNullable(accessInfo.getBeforeDate())
                    .orElse(new Date(currentTimeMillis + config.getMaxRefreshTokenExp()));
            Algorithm algorithm = Algorithm.HMAC256(secretBytes);
            // 附带username，nickname信息
            String sign = JWT.create()
                    .withJWTId(accessInfo.getUserKey())
                    .withExpiresAt(date)
                    .withClaim(beforeKey, beforeDate)
                    .withClaim(extendKey, accessInfo.getExtend())
                    .withClaim(platformKey, accessInfo.getPlatform().name())
                    .sign(algorithm);
            String bearer = config.getBearer();
            return StringUtils.hasText(bearer) ? bearer + sign : sign;
        } catch (JWTCreationException e) {
            return null;
        }
    }

    /**
     * 设置cookie : 登录和刷新
     *
     * @param request
     * @param response
     * @param accessInfo
     * @return token
     */
    public Tokens setCookie(HttpServletRequest request, HttpServletResponse response, AccessInfo accessInfo,
                            JWTConfig config) {
        Tokens tokens = getTokens(accessInfo, config);
        Cookie cookie = new Cookie(config.getToken(), tokens.getToken());
        cookie.setMaxAge((int) config.getTokenExp() / 1000);

        Cookie refreshCookie = new Cookie(config.getRefreshToken(), tokens.getRefreshToken());
        refreshCookie.setMaxAge((int) (config.getRefreshTokenExp() / 1000));
        String host = request.getServerName();

        if (!LOCALHOST.equals(host)) {
            cookie.setDomain(host.substring(host.indexOf(".") + 1));
            refreshCookie.setDomain(host.substring(host.indexOf(".") + 1));
        }
        refreshCookie.setPath("/");
        cookie.setPath("/");

        cookie.setSecure(config.getSecurity());
        refreshCookie.setSecure(config.getSecurity());
        cookie.setHttpOnly(config.getHttpOnly());
        refreshCookie.setHttpOnly(config.getHttpOnly());
        response.addCookie(cookie);
        response.addCookie(refreshCookie);

        return tokens;
    }

    public Tokens getTokens(AccessInfo accessInfo, JWTConfig config) {
        Tokens tokens = new Tokens();
        String token = signToken(accessInfo, config);
        String refreshToken = signRefreshToken(accessInfo, config);
        tokens.setToken(token);
        tokens.setRefreshToken(refreshToken);
        return tokens;
    }

    public Tokens getTokens(HttpServletRequest request, HttpServletResponse response, JWTConfig config) {
        Tokens tokens = new Tokens();
        String token = getToken(request, config.getToken());
        tokens.setToken(token);
        String refreshToken = getToken(request, config.getRefreshToken());
        tokens.setRefreshToken(refreshToken);
        return tokens;
    }

    private String getToken(HttpServletRequest request, String key) {
        String token = request.getHeader(key);
        if (!StringUtils.hasText(token)) {
            // 优先考虑使用cookie 作为载体
            Cookie[] cookies = request.getCookies();
            if (null != cookies) {
                for (Cookie cookie : cookies) {
                    if (key.equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }
        return token;
    }

    /**
     * 从请求中获取cookie
     *
     * @param httpServletRequest
     * @param cookieName
     * @return
     */
    public static Cookie getCookie(HttpServletRequest httpServletRequest, String cookieName) {

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


    public void clearCookie(HttpServletRequest request, HttpServletResponse response, JWTConfig config) {
        Cookie cookie = new Cookie(config.getToken(), "");
        cookie.setMaxAge(0);
        Cookie refreshCookie = new Cookie(config.getRefreshToken(), "");
        refreshCookie.setMaxAge(0);
        cookie.setHttpOnly(config.getHttpOnly());
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
