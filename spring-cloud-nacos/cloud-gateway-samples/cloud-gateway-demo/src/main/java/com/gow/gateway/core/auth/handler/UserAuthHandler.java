package com.gow.gateway.core.auth.handler;

import com.alibaba.fastjson.JSONObject;
import com.gow.gateway.core.auth.AuthHandler;
import com.gow.gateway.core.constant.GatewayConstants;
import com.gow.gateway.core.domain.auth.AuthDomain;
import com.gow.gateway.core.domain.auth.token.AuthToken;
import com.gow.gateway.core.domain.auth.token.UserAuthToken;
import com.gow.gateway.core.spi.auth.AuthServiceFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author gow
 * @date 2021/7/17 0017
 */
@Slf4j
public class UserAuthHandler implements AuthHandler {

    private final AuthServiceFacade authServiceFacade;

    public UserAuthHandler(AuthServiceFacade authServiceFacade) {
        this.authServiceFacade = authServiceFacade;
    }

    @Override
    public Mono<AuthToken> checkAndGetToken(ServerWebExchange exchange) {
        return Mono.defer(() -> {
            String authToken = exchange.getRequest().getHeaders().getFirst(GatewayConstants.AUTH_TOKEN_HEADER);
            UserAuthToken token = JSONObject.parseObject(authToken, UserAuthToken.class);
            if (token == null) {
                return Mono.empty();
            } else {
                if (StringUtils.isEmpty(token.getUserKey()) || StringUtils.isEmpty(token.getPassword())
                        || token.getTokenType() == null) {
                    log.info("auth token error,user={},token={},type={}", token.getUserKey(), token.getPassword(),
                            token.getTokenType());
                    return Mono.empty();
                }
                return Mono.just(token);
            }
        });
    }

    @Override
    public  Mono<Boolean> authenticate(AuthToken authToken) {
        return authServiceFacade.userAuthenticate((UserAuthToken) authToken);
    }

    @Override
    public boolean support(String authDomain) {
        return AuthDomain.user.getId().equals(authDomain);
    }
}
