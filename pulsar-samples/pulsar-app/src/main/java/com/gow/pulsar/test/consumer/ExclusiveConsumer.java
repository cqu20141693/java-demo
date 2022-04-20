package com.gow.pulsar.test.consumer;

import org.apache.pulsar.client.api.PulsarClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/7/7
 */
@Component
public class ExclusiveConsumer implements ApplicationRunner {

    @Autowired
    private PulsarClient pulsarClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}
