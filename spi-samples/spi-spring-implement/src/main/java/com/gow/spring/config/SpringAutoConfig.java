package com.gow.spring.config;

import com.gow.spring.spi.util.SpringServiceLoader;
import com.wujt.spi.Developer;
import com.wujt.spi.MessageProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gow
 * @date 2021/6/23
 */
@Configuration
public class SpringAutoConfig {

    @Bean
    public Developer developer() {
        return SpringServiceLoader.getSpringFactoriesInstances(Developer.class);
    }

    @Bean
    public MessageProducer messageProducer() {
        return SpringServiceLoader.getSpringFactoriesInstances(MessageProducer.class);
    }

}
