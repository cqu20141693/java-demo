package com.gow.gateway.core.filter;

import com.gow.gateway.core.constant.GatewayConstants;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author gow
 * @date 2021/9/10
 */
@Component
public class AuthHeaderRemoveFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(httpHeaders -> {
                    httpHeaders.remove(GatewayConstants.AUTH_DOMAIN_HEADER);
                    httpHeaders.remove(GatewayConstants.AUTH_TOKEN_HEADER);
                    httpHeaders.remove(GatewayConstants.NON_AUTH_HEADER);
                }).build();

        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 1000;
    }
}
