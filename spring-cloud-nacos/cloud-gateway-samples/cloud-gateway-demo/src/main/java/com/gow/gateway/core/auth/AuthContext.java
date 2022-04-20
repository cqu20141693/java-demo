package com.gow.gateway.core.auth;

import com.gow.gateway.core.auth.handler.AppAuthHandler;
import com.gow.gateway.core.auth.handler.DeviceAuthHandler;
import com.gow.gateway.core.auth.handler.GroupAuthHandler;
import com.gow.gateway.core.auth.handler.UserAuthHandler;
import com.gow.gateway.core.spi.auth.AuthServiceFacade;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author gow
 * @date 2021/7/17 0017
 */
@Component
@Slf4j
public class AuthContext {

    private final List<AuthHandler> authHandlers = new ArrayList<>();

    public AuthContext(AuthServiceFacade authServiceFacade) {
        authHandlers.add(new UserAuthHandler(authServiceFacade));
        authHandlers.add(new GroupAuthHandler(authServiceFacade));
        authHandlers.add(new DeviceAuthHandler(authServiceFacade));
        authHandlers.add(new AppAuthHandler(authServiceFacade));

    }

    public Mono<AuthHandler> getAuthHandler(String authDomain) {
        for (AuthHandler authHandler : authHandlers) {
            if(authHandler.support(authDomain)){
                return Mono.just(authHandler);
            }
        }
        return Mono.empty();
    }

}
