package com.cc.minio.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static com.cc.minio.config.MinioConfig.PREFIX_PROPERTIES;

@Configuration
@ConfigurationProperties(prefix = PREFIX_PROPERTIES)
@Data
public class MinioConfig {

    public static final String PREFIX_PROPERTIES = "cc.minio";


    /**
     * minio URL
     */
    private String endPoint;

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * secretKey
     */
    private String secretKey;

    /**
     * 访问地址
     */
    private String requestUrl;

}
