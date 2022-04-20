package com.gow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author gow
 * @date 2021/7/3 0003
 */
@SpringBootApplication
@EnableCaching
public class SpringCacheApp {
    public static void main(String[] args) {
        SpringApplication.run(SpringCacheApp.class);
    }
}
