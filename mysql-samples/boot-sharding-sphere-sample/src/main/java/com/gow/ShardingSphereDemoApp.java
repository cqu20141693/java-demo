package com.gow;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author wujt
 */
@SpringBootApplication
@MapperScan
@EnableTransactionManagement
public class ShardingSphereDemoApp {
    public static void main(String[] args) {
        SpringApplication.run(ShardingSphereDemoApp.class,args);
    }
}
