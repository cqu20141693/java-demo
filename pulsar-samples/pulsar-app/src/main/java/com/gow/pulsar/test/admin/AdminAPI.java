package com.gow.pulsar.test.admin;

import org.apache.pulsar.client.admin.PulsarAdmin;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/7/7
 */
@Component
public class AdminAPI implements ApplicationRunner {

    private PulsarAdmin pulsarAdmin;

    public AdminAPI(PulsarAdmin pulsarAdmin) {
        this.pulsarAdmin = pulsarAdmin;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
    }
}
