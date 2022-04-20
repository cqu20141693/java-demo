package com.gow.pulsar.test.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

/**
 * default topic must be producer shared
 *
 * @author gow
 * @date 2021/7/7
 */
@Component("batchProducer")
@ConditionalOnProperty(prefix = "gow.test.pulsar",name = "batchProducer",havingValue = "true")
public class BatchProducer implements ApplicationRunner {


    @Override
    public void run(ApplicationArguments args) throws Exception {
    }

}
