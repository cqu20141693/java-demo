package com.gow.gateway.starter.spi.impl.auth;

import com.gow.common.Result;
import com.gow.gateway.core.domain.auth.DeviceTokenType;
import com.gow.gateway.core.domain.auth.token.AppAuthToken;
import com.gow.gateway.core.domain.auth.token.DDeviceAuthToken;
import com.gow.gateway.core.domain.auth.token.DeviceBaseAuthToken;
import com.gow.gateway.core.domain.auth.token.GDeviceAuthToken;
import com.gow.gateway.core.domain.auth.token.GroupAuthToken;
import com.gow.gateway.core.domain.auth.token.UserAuthToken;
import com.gow.gateway.core.spi.auth.AuthServiceFacade;
import com.gow.gateway.starter.config.WebClientUriConfig;
import com.wujt.backend.domain.authenticate.DDeviceAuthReq;
import com.wujt.backend.domain.authenticate.TokenType;
import com.wujt.backend.domain.authenticate.UserAuthReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author gow
 * @date 2021/7/18 0018
 */
@Component
@Slf4j
public class AuthServiceFacadeImpl implements AuthServiceFacade {

//    @Autowired
//    private AuthenticateClient authenticateClient;

    @Autowired
    private WebClient webClient;
    @Autowired
    private WebClientUriConfig webClientUriConfig;

    @Override
    public Mono<Boolean> userAuthenticate(UserAuthToken userAuthToken) {
        // feign client : blocking client when load balance instance
        // direct use feign client throw exception: block()/blockFirst()/blockLast() are blocking, which is not
        // supported in thread reactor-http-nio-3
        // solve plan
        // WebClient : spring cloud support LoadBalancedExchangeFilterFunction
        UserAuthReq userAuthReq = new UserAuthReq();
        userAuthReq.setUserKey(userAuthToken.getUserKey());
        userAuthReq.setTokenType(TokenType.valueOf(userAuthToken.getTokenType().name()));
        userAuthReq.setPassword(userAuthToken.getPassword());
        Mono<Result<Boolean>> result = doUserAuthenticate(userAuthReq);
        return result.map(ret -> {
            if (ret.success()) {
                return ret.getBody();
            }
            return false;
        });
    }


    @Override
    public Mono<Boolean> appAuthenticate(AppAuthToken appAuthToken) {
        return Mono.just(true);
    }

    @Override
    public Mono<Boolean> deviceAuthenticate(DeviceBaseAuthToken deviceBaseAuthToken) {
        if (deviceBaseAuthToken.getDeviceTokenType() == DeviceTokenType.one_type_one_key) {
            GDeviceAuthToken gDeviceAuthToken = (GDeviceAuthToken) deviceBaseAuthToken;
            return Mono.just(true);
        } else if (deviceBaseAuthToken.getDeviceTokenType() == DeviceTokenType.one_machine_one_key) {
            DDeviceAuthToken dDeviceAuthToken = (DDeviceAuthToken) deviceBaseAuthToken;
            DDeviceAuthReq req = new DDeviceAuthReq();
            req.setTokenType(TokenType.valueOf(dDeviceAuthToken.getTokenType().name()));
            req.setDeviceKey(dDeviceAuthToken.getDeviceKey());
            req.setDeviceToken(dDeviceAuthToken.getDeviceToken());
            Mono<Result<Boolean>> result = doDDeviceAuthenticate(req);
            return result.map(ret -> {
                if (ret.success()) {
                    return ret.getBody();
                }
                return false;
            });
        }
        log.info("deviceAuthenticate deviceTokenType={} not support", deviceBaseAuthToken.getDeviceTokenType());
        return Mono.just(false);
    }


    @Override
    public Mono<Boolean> groupAuthenticate(GroupAuthToken groupAuthToken) {
        return Mono.just(true);
    }

    private Mono<Result<Boolean>> doUserAuthenticate(UserAuthReq userAuthReq) {
        return webClient.post().uri(webClientUriConfig.getUserAuthUri())
                .body(Mono.just(userAuthReq), UserAuthReq.class)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Result<Boolean>>() {
                });
    }

    private Mono<Result<Boolean>> doDDeviceAuthenticate(DDeviceAuthReq req) {
        return webClient.post().uri(webClientUriConfig.getDDeviceAuthUri())
                .body(Mono.just(req), DDeviceAuthReq.class)
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(new ParameterizedTypeReference<Result<Boolean>>() {
                        });
                    } else if (response.statusCode().is4xxClientError()) {
                        log.info(" occur 4xxClientError");
                        // Suppress error status code
                        return Mono.just(Result.ok(false));
                    } else {
                        log.info("request server occur exception");
                        // Turn to error
                        return response.createException().flatMap(Mono::error);
                    }
                });
    }
}
