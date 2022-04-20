package com.gow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wujt  2021/5/25
 */
@SpringBootApplication
public class GrpcApp {
    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        SpringApplication.run(GrpcApp.class, args);
    }
}
