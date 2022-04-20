package com.gow.gateway.core.filter;

import com.gow.gateway.core.auth.AuthContext;
import com.gow.gateway.core.auth.AuthHandler;
import com.gow.gateway.core.constant.GatewayConstants;
import com.gow.gateway.core.domain.auth.token.AuthToken;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author gow
 * @date 2021/7/17 0017
 */
@Slf4j
@Component
public class GlobalAuthFilter implements GlobalFilter, Ordered {
    @Autowired
    private AuthContext authContext;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String nonAuth = exchange.getRequest().getHeaders().getFirst(GatewayConstants.NON_AUTH_HEADER);
        if (!StringUtils.isEmpty(nonAuth)) {
            ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
            builder.headers(httpHeaders -> {
                httpHeaders.remove(GatewayConstants.NON_AUTH_HEADER);
            });
            return chain.filter(exchange.mutate().request(builder.build()).build());
        }
        String authDomain = exchange.getRequest().getHeaders().getFirst(GatewayConstants.AUTH_DOMAIN_HEADER);

        Mono<AuthHandler> authHandler = authContext.getAuthHandler(authDomain);

     return  authHandler.hasElement().flatMap(exist->{
         if(exist){
            return authHandler.flatMap(h->{
                 Mono<AuthToken> token = h.checkAndGetToken(exchange);
                return token.hasElement().flatMap(has->{
                     if (has){
                         return token.flatMap(t-> h.authenticate(t).flatMap(result->{
                              if(result){
                                  return chain.filter(exchange);
                              }
                              exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                              return exchange.getResponse().setComplete();
                          }));
                     }
                     exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                     return exchange.getResponse().setComplete();
                 });

             });
         }
         exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
         return exchange.getResponse().setComplete();
     }) ;
    }

    @Override
    public int getOrder() {

        return HIGHEST_PRECEDENCE + 100;
    }
}
