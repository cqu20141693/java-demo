package com.gow.gateway.core.auth;

import com.gow.gateway.core.domain.auth.token.AuthToken;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author gow
 * @date 2021/7/12 0012
 */
public interface AuthHandler {

    /**
     * Validate the request and obtain the Auth Token
     *
     * @param exchange
     * @return
     */
    Mono<AuthToken> checkAndGetToken(ServerWebExchange exchange);

    /**
     * Authenticate the Auth Token
     *
     * @param authToken
     * @return
     */
    Mono<Boolean> authenticate(AuthToken authToken);

    /**
     * support auth domain
     *
     * @param authDomain
     * @return
     */
    boolean support(String authDomain);

}
