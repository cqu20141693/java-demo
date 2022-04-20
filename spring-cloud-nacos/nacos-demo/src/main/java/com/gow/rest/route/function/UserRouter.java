package com.gow.rest.route.function;

import com.gow.rest.route.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class UserRouter {

    @Bean
    public RouterFunction<ServerResponse> login(UserHandler userHandler) {
        return RouterFunctions
                .route(RequestPredicates.POST("/user/login")
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        userHandler::login);
    }

    @Bean
    public RouterFunction<ServerResponse> list(UserHandler userHandler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/user/list"),
                        userHandler::list);
    }

    @Bean
    public RouterFunction<ServerResponse> getUserInfo(UserHandler userHandler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/user/{userKey}/getUserInfo"),
                        userHandler::getUserInfo);
    }
}
