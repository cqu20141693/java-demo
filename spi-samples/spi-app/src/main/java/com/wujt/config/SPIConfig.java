package com.wujt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author wujt
 */
@Configuration
@ConfigurationProperties(prefix = "developer")
@Data
public class SPIConfig {
    List<AdapterConfig> adapters;
}
