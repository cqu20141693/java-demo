package com.wcc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import reactor.core.publisher.Mono;

/**
 * @author wujt
 */
@SpringBootApplication
@Slf4j
@EnableCassandraRepositories
public class WebFluxMonoApp implements CommandLineRunner {


    public static void main(String[] args) {


        SpringApplication.run(WebFluxMonoApp.class, args);
    }


    @Override
    public void run(String... args) {

        String block = Mono.just("hello").block();
        log.info("block={}", block);
    }
}
