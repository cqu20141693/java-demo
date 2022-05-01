package com.cc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class DeviceRegistry {
    private final Map<String, Object> cache = new HashMap<String, Object>();

    public Flux<Boolean> setConfig(String key, String name) {
        log.info("setConfig key={},name={}", key, name);
        cache.put(key, name);
        return Flux.just(true);
    }

    public Mono<String> getSelfConfig(String key) {
        return Mono.just((String) cache.get(key));
    }
}
