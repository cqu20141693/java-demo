package com.wcc.openapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * wcc 2022/8/26
 */
@SpringBootApplication
public class OpenApiApp implements CommandLineRunner {

    @Value("${server.port:8080}")
    private String port;
    public static void main(String[] args) {
        SpringApplication.run(OpenApiApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("文档地址：http://localhost:"+port+"/doc.html");
    }
}
