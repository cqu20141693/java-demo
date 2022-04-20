package com.cc.link.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author gow
 * @date 2022/2/16
 */
@Configuration
@ConfigurationProperties(prefix = "cc.sip")
@Data
public class SipConfig {
    private String host = "0.0.0.0";
    private Integer port = 5060;
}
