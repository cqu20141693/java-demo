package com.wujt;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wujt
 */
@SpringBootApplication
public class MqttApp implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(MqttApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
