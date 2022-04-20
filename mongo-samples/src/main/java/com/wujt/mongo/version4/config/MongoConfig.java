package com.wujt.mongo.version4.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wujt
 * @version 1.0
 * @date 2020/3/16
 */
@Configuration
@ConfigurationProperties(prefix = "mongo")
@Data
public class MongoConfig {
    private String uri;
    private String dbName;

}
