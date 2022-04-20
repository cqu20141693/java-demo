package com.gow.gateway.core.spi.auth;

import com.gow.gateway.core.domain.auth.token.AppAuthToken;
import com.gow.gateway.core.domain.auth.token.DeviceBaseAuthToken;
import com.gow.gateway.core.domain.auth.token.GroupAuthToken;
import com.gow.gateway.core.domain.auth.token.UserAuthToken;
import reactor.core.publisher.Mono;

/**
 * @author gow
 * @date 2021/7/18 0018
 */
public interface AuthServiceFacade {

    Mono<Boolean> userAuthenticate(UserAuthToken userAuthToken);

    Mono<Boolean> appAuthenticate(AppAuthToken appAuthToken);

    Mono<Boolean> deviceAuthenticate(DeviceBaseAuthToken deviceBaseAuthToken);

    Mono<Boolean> groupAuthenticate(GroupAuthToken groupAuthToken);

}
