package com.wujt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wujt
 */
@SpringBootApplication
@Slf4j
public class KafkaApp implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(KafkaApp.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("{} class is start", KafkaApp.class);
    }
}
