package com.cc.core.config;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.boot.autoconfigure.elasticsearch.RestClientBuilderCustomizer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EsRestClientBuilderCustomizer implements RestClientBuilderCustomizer {
    @Override
    public void customize(RestClientBuilder builder) {
        log.info("es rest client customizer");
    }
}
