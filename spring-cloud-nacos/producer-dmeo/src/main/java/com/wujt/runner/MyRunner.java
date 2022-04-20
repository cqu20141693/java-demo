package com.wujt.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;

/**
 * @author wujt  2021/6/2
 */
@Slf4j
public class MyRunner implements CommandLineRunner, ApplicationRunner, Ordered {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("MyRunner ApplicationRunner method invoked,args={}",args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("MyRunner CommandLineRunner method invoked,args={}",args);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 10;
    }
}
