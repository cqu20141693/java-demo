package com.gow;

import com.gow.pulsar.core.annotation.EnablePulsar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author gow
 * @date 2021/7/19
 */
@SpringBootApplication
@EnablePulsar
@EnableScheduling
public class PulsarStarter {
    public static void main(String[] args) {
        SpringApplication.run(PulsarStarter.class, args);
    }
}
