package com.gow.gateway.core.filter;

import com.gow.gateway.core.config.NonAuthPathConfig;
import com.gow.gateway.core.constant.GatewayConstants;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author gow
 * @date 2021/7/17 0017
 */
@Component
public class GlobalNonAuthFilter implements GlobalFilter, Ordered {

    private NonAuthPathConfig nonAuthPathConfig;

    public GlobalNonAuthFilter(NonAuthPathConfig nonAuthPathConfig) {
        this.nonAuthPathConfig = nonAuthPathConfig;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
        builder.headers(httpHeaders -> {
            httpHeaders.remove(GatewayConstants.NON_AUTH_HEADER);
        });
        RequestPath path = exchange.getRequest().getPath();
        if (nonAuthPathConfig.getPaths().contains(path.value())) {
            builder.headers(httpHeaders -> {
                httpHeaders.add(GatewayConstants.NON_AUTH_HEADER, "non-auth");
            });
        }
        return chain.filter(exchange.mutate().request(builder.build()).build());
    }

    @Override
    public int getOrder() {

        return HIGHEST_PRECEDENCE + 99;
    }
}
