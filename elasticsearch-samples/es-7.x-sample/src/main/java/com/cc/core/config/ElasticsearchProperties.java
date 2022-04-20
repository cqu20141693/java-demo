package com.cc.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ConfigurationProperties(prefix = "cc.elasticsearch")
@Data
public class ElasticsearchProperties {
    private List<String> uris = new ArrayList<>(Collections.singletonList("http://localhost:9200"));

    /**
     * Credentials username.
     */
    private String username;

    /**
     * Credentials password.
     */
    private String password;

    /**
     * Connection timeout.
     */
    private Duration connectionTimeout = Duration.ofSeconds(1);

    /**
     * Read timeout.
     */
    private Duration readTimeout = Duration.ofSeconds(30);
}
