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
@ConfigurationProperties(prefix = "mqtt")
@Data
public class MqttConfig {
    private String version;
    private String environment;
    private LocalConfig local;
    private DevConfig dev;
    public String getHost() {
        if(environment.equalsIgnoreCase("dev")){
            return dev.getHost();
        }
        return local.getHost();
    }

    public int getPort() {
        if(environment.equalsIgnoreCase("dev")){
            return dev.getPort();
        }
        return local.getPort();
    }

    public String getClientId() {
        if(environment.equalsIgnoreCase("dev")){
            return dev.getClientId();
        }
        return local.getClientId();
    }

    public String getUserName() {
        if(environment.equalsIgnoreCase("dev")){
            return dev.getUserName();
        }
        return local.getUserName();
    }

    public String getPassword() {
        if(environment.equalsIgnoreCase("dev")){
            return dev.getPassword();
        }
        return local.getPassword();
    }
}
