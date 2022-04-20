package com.gow.gateway.core.auth.handler;

import com.gow.gateway.core.auth.AuthHandler;
import com.gow.gateway.core.domain.auth.AuthDomain;
import com.gow.gateway.core.domain.auth.token.AppAuthToken;
import com.gow.gateway.core.domain.auth.token.AuthToken;
import com.gow.gateway.core.spi.auth.AuthServiceFacade;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author gow
 * @date 2021/7/17 0017
 */
public class AppAuthHandler implements AuthHandler {

    private final AuthServiceFacade authServiceFacade;

    public AppAuthHandler(AuthServiceFacade authServiceFacade) {
        this.authServiceFacade = authServiceFacade;
    }

    @Override
    public Mono<AuthToken> checkAndGetToken(ServerWebExchange exchange) {
        return Mono.empty();
    }

    @Override
    public  Mono<Boolean> authenticate(AuthToken authToken) {
        return authServiceFacade.appAuthenticate((AppAuthToken) authToken);
    }

    @Override
    public boolean support(String authDomain) {
        return AuthDomain.app.getId().equals(authDomain);
    }
}
