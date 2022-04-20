package com.gow.device.operation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author gow
 * @date 2021/7/14 0014
 */
@SpringBootApplication
@MapperScan
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
