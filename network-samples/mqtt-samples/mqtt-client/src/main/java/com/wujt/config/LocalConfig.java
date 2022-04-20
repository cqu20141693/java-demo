package com.wujt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wujt
 * @version 1.0
 * @date 2020/3/16
 */
@Data
@Configuration
@ConfigurationProperties("mqtt.local")
public class LocalConfig {
    private String host;
    private Integer port;
    private String clientId;
    private String userName;
    private String password;
}
