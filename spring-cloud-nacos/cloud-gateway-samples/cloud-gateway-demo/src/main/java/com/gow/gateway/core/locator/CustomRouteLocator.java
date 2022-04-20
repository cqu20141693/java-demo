package com.gow.gateway.core.locator;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wujt  2021/6/2
 */
@Configuration
public class



CustomRouteLocator {
   // @Bean
    public RouteLocator myRouteLocator(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route("path_route", r -> r.order(500).path("/test").filters(f -> f.retry(1)).uri("http://localhost:8889/test"))
                .build();
    }

}
