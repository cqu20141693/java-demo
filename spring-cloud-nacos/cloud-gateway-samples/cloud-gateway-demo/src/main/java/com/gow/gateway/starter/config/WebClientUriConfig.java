package com.gow.gateway.starter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author gow
 * @date 2021/7/18 0018
 */
@Configuration
@ConfigurationProperties(prefix = "com.gow.gateway.web-client-uri")
@Data
public class WebClientUriConfig {
    private String userAuthUri;
    private String dDeviceAuthUri;
    private String gDeviceAuthUri;
    private String appAuthUri;
    private String groupAuthUri;
}
