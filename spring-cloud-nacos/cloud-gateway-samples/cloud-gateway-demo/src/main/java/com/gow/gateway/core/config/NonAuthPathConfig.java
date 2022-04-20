package com.gow.gateway.core.config;

import java.util.Set;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author gow
 * @date 2021/7/17 0017
 */
@Configuration
@ConfigurationProperties(prefix = "com.gow.gateway.non-auth")
@Data
@RefreshScope
public class NonAuthPathConfig {
    /**
     * non auth paths : consider use Spring AntPattern
     */
    private Set<String> paths;

}
