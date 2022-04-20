package com.wujt;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wujt
 * @version 1.0
 * @date 2020/3/20
 */
@SpringBootApplication
public class RabbitMQApp implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMQApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {


    }
}
