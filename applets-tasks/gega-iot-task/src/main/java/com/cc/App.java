package com.cc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.cc", "com.gow"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
