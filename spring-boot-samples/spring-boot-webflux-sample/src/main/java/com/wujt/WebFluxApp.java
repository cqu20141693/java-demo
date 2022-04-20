package com.wujt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Mono;

/**
 * @author wujt
 */
@SpringBootApplication(scanBasePackages = {"com.gow","com.wujt"})
@Slf4j
public class WebFluxApp implements CommandLineRunner {


    public static void main(String[] args) {


        SpringApplication.run(WebFluxApp.class, args);
    }


    @Override
    public void run(String... args) {
        String block = Mono.just("hello").block();
        log.info("block={}", block);
    }
}
