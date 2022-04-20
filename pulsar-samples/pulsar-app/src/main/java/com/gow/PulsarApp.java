package com.gow;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author gow
 * @date 2021/7/2
 */
@SpringBootApplication
@EnableScheduling
@MapperScan("com.gow.pulsar.manager.gen")
public class PulsarApp {
    public static void main(String[] args) {
        SpringApplication.run(PulsarApp.class);
    }
}
