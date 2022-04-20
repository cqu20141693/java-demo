package com.cc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author gow
 * @date 2022/2/28
 */
@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "com.cc.core.repository")
public class ESApp {
    public static void main(String[] args) {
        SpringApplication.run(ESApp.class);
    }
}
