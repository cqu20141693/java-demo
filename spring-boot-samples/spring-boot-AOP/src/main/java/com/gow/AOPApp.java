package com.gow;

import com.gow.aop.aspectj.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author gow
 * @date 2021/7/22 0022
 */
@SpringBootApplication
public class AOPApp implements CommandLineRunner {
    @Autowired
    private CommonService commonService;

    public static void main(String[] args) {
        SpringApplication.run(AOPApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        commonService.greeting();
        commonService.welcome();
        try {
            commonService.exception();
        } catch (Exception e) {
            System.out.println("exception occur " + e.getMessage());
        }

    }
}
