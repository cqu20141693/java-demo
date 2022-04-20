package com.wujt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wujt  2021/4/29
 */
@Data
@Configuration
@ConfigurationProperties("kafka.topic")
public class TopicConfig {

    private String dataTopic;
}
