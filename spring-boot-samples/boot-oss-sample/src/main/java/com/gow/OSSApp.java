package com.gow;

import com.gow.event.MyListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wujt  2021/5/19
 */
@SpringBootApplication
public class OSSApp {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(OSSApp.class);
        springApplication.addListeners(new MyListener());
        springApplication.run(args);
    }
}
