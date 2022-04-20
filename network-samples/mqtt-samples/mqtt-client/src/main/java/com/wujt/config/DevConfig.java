package com.wujt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wujt
 * @version 1.0
 * @date 2020/3/16
 */
@Configuration
@ConfigurationProperties("mqtt.dev")
@Data
public class DevConfig {
    private String host;
    private Integer port;
    private String clientId;
    private String userName;
    private String password;
}
