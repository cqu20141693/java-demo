package com.gow.rest.route.handler;

import com.gow.user.model.LoginInfo;
import com.gow.user.model.UserInfo;
import com.gow.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class UserHandler {
    @Autowired
    private UserService userService;

    public Mono<ServerResponse> login(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(Objects.requireNonNull(request.bodyToMono(LoginInfo.class).handle(((loginInfo, synchronousSink) -> {
                    log.info("loginInfo={]", loginInfo);
                    synchronousSink.next(loginInfo);
                    synchronousSink.complete();
                })).block()), LoginInfo.class);
    }

    public Mono<ServerResponse> list(ServerRequest request) {
        Optional<String> userKey = request.queryParam("userKey");
        log.info("userKey={}", userKey);
        return ServerResponse.ok().body(Flux.fromStream(userService.list().stream()), UserInfo.class);
    }

    public Mono<ServerResponse> getUserInfo(ServerRequest serverRequest) {
        String userKey = serverRequest.pathVariable("userKey");
        log.info("userKey={}", userKey);
        return ServerResponse.ok().body(Mono.just(userService.getUserInfo()), UserInfo.class);
    }
}
