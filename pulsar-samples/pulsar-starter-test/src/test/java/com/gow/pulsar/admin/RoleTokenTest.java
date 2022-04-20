package com.gow.pulsar.admin;

import com.gow.pulsar.core.manager.token.AuthTokenUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.common.util.RelativeTimeUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author gow
 * @date 2021/8/26
 */
@Slf4j
public class RoleTokenTest {
    // Secret: bin/pulsar tokens create-secret-key --output /pulsar/data/my-secret.key --base64
    // Token: bin/pulsar tokens create --secret-key file:///pulsar/data/my-secret.key --subject cc-tech-super-user

    // dev-secret: P+jNNI/9ICiCSvdIU17szob6EdMQviY69CB3DQpZ8QA=
    // dev-super-role: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaG9uZ2N0ZWNoLXN1cGVyLXVzZXIifQ
    // .o8GzC_pMaIayEJJxwcfQsTLRuup6eL0idBFgiSJLjmo(cc-tech-super-user)

    // test-secret: YzDPBlf6faU7p8JWApH6+g7inGL7lqIO/GARkkdVZ8g=
    // test-super-role: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaG9uZ2N0ZWNoLXN1cGVyIn0
    // .8g2EUfFzKtVKEeQzyLJWuVGDCNU4dY-hxciYZ_v--n8(cc-tech-super)
    // prod-secret:

    // secret
    //    private static final String secret = "P+jNNI/9ICiCSvdIU17szob6EdMQviY69CB3DQpZ8QA=";
    private static final String secret = "YzDPBlf6faU7p8JWApH6+g7inGL7lqIO/GARkkdVZ8g=";


    // super Token
    //    private static final String superToken =
//            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaG9uZ2N0ZWNoLXN1cGVyLXVzZXIifQ"
//                    + ".o8GzC_pMaIayEJJxwcfQsTLRuup6eL0idBFgiSJLjmo";
    private static final String superToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaG9uZ2N0ZWNoLXN1cGVyIn0.8g2EUfFzKtVKEeQzyLJWuVGDCNU4dY-hxciYZ_v--n8";

    private static final String adminToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjY3RlY2gifQ.TkCwKZIW-CCQEI6qjoAnEImpJzDymkYthWUymGQwArg";

    private static final String gowToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJnb3cifQ.XEHjhJUU-yx6z-fOFid2K-Ez3EK-tlLUt4Ny0sk308A";

    private static final String wujtToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3dWp0In0.olocLTxUhg-4m5EtEOXocxC0ZrfunO7_a7f91ehzWTQ";


    @Test
    @ParameterizedTest
    @ValueSource(strings = {secret})
    public void testBase64(String value) {
        log.info("value={}, base64={}", value,
                Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 利用superToken创建adminRole Token
     * 后续需要进行租户授权
     */
    @Test
    @DisplayName("test createSuperToken")
    public void createSuperToken() {
        String superToken = createBrokerToken("cc-tech-super", null);
        if (superToken.equals(RoleTokenTest.superToken)) {
            log.info("admin token cc-tech add success");
        } else {
            log.info("admin token cc-tech add failed");
        }
    }

    @Test
    @DisplayName("test createAdminToken")
    public void createAdminToken() {
        String cc-techToken = createBrokerToken("cc-tech", null);
        if (cc-techToken.equals(adminToken)) {
            log.info("admin token cc-tech add success");
        } else {
            log.info("admin token cc-tech add failed");
        }
    }

    /**
     * 利用adminToken 创建subscribeRole Token
     * 后续需要创建topic,并进行topic consumer 授权
     */
    @Test
    @DisplayName("test createSubRoleToken")
    public void createSubRoleToken() {

        String gowToken = createBrokerToken("gow", null);
        if (gowToken.equals(RoleTokenTest.gowToken)) {
            log.info("subscribe role gow token add success");
        } else {
            log.info("subscribe role gow token add failed");
        }
    }

    @Test
    @DisplayName("test createSubRoleToken")
    public void parseSubRoleToken() {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        Key signingKey = AuthTokenUtils.decodeSecretKey(bytes);
        Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(wujtToken);
        Optional<String> subject = Optional.ofNullable(jws.getBody().getSubject());
        assert "wujt".equals(subject.orElse("")) : "jwt error";

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(gowToken);
        Optional<String> sub = Optional.ofNullable(claimsJws.getBody().getSubject());
        log.info("role={}", sub.orElse(""));
    }


    public String createBrokerToken(String role, String expiryTime) {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        Key signingKey = AuthTokenUtils.decodeSecretKey(bytes);
        Optional<Date> optExpiryTime = Optional.empty();
        if (expiryTime != null) {
            long relativeTimeMillis = TimeUnit.SECONDS
                    .toMillis(RelativeTimeUtil.parseRelativeTimeInSeconds(expiryTime));
            optExpiryTime = Optional.of(new Date(System.currentTimeMillis() + relativeTimeMillis));
        }
        return AuthTokenUtils.createToken(signingKey, role, optExpiryTime);
    }
}
