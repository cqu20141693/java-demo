package com.gow;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author gow
 * @date 2021/7/2 0002
 */
@SpringBootApplication
public class SpELApp implements CommandLineRunner {

    @Value("${pulsar.random}")
    private String random;
    @Value("${pulsar.uuid}")
    private String uuid;
    public static void main(String[] args) {
        SpringApplication.run(SpELApp.class);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("random=" + random);
        System.out.println("uuid=" + uuid);
    }
}
