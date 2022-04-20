package com.wujt;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App implements CommandLineRunner {
    public static void main( String[] args ) {
       SpringApplication app = new SpringApplication( App.class );
       String test="{\"int\":1,\"o\":\"test\"}";
        app.run( args );
    }
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Elastic job start !");
    }
}
