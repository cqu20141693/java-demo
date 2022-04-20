package com.wujt;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author wujt
 * @version 1.0
 * @date 2020/2/3
 * @description : 启动类
 */

@SpringBootApplication
@EnableTransactionManagement
public class MongoApp implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(MongoApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
