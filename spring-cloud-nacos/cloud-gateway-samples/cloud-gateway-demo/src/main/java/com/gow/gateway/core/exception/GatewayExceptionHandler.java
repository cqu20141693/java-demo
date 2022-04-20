package com.gow.gateway.core.exception;

import com.alibaba.fastjson.JSON;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author gow
 * @date 2021/9/10
 */
@Slf4j
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        int code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String errMsg;
        if(ex instanceof OpenGatewayException){
            errMsg = ex.getMessage();
        } else if (ex instanceof NotFoundException) {
            code = HttpStatus.NOT_FOUND.value();
            errMsg = ((NotFoundException) ex).getReason();
        } else if (ex instanceof ResponseStatusException) {
            code = ((ResponseStatusException) ex).getStatus().value();
            errMsg = ((ResponseStatusException) ex).getMessage();
        } else {
            errMsg = ex.getMessage();
        }

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.resolve(code));
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("message", errMsg);
        log.error("全局异常处理：{}", JSON.toJSONString(map));
        DataBuffer dataBuffer =
                response.bufferFactory().allocateBuffer().write(JSON.toJSONString(map).getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(dataBuffer));
    }
}
