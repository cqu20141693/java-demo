package com.cc.minio.config;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Bean
    public MinioClient minioClient(MinioConfig config) {
        return MinioClient.builder()
                .endpoint(config.getEndPoint())
                .credentials(config.getAccessKey(), config.getSecretKey())
                .build();
    }
}
