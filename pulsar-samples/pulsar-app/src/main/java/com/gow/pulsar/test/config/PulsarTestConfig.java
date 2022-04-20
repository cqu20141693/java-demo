package com.gow.pulsar.test.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author gow
 * @date 2021/7/7
 */
@Configuration
@ConfigurationProperties(prefix = "gow.test.pulsar")
@Data
public class PulsarTestConfig {
    private Boolean jsonSchemaTopic = false;
    private Boolean failOver = false;
}
