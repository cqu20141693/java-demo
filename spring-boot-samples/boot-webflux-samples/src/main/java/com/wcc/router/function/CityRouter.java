package com.wcc.router.function;

import com.wcc.router.handler.CityHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


/**
 * @author wujt
 */
@Configuration
public class CityRouter {
    @Bean
    public RouterFunction<ServerResponse> routeCity(CityHandler cityHandler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/hello"),
                        cityHandler::helloCity);
    }

}
