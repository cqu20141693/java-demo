package com.wujt.config;

import lombok.Data;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wujt
 * @version 1.0
 * @date 2020/3/20
 */
@Data
@Configuration
@ConfigurationProperties("rabbit")
public class RabbitMqInfo {

    private String host;
    private Integer port;
    private String username;
    private String password;
    private String virtualHost;
    private CachingConnectionFactory.ConfirmType publisherConfirms;
    private Boolean publishReturns;
}
