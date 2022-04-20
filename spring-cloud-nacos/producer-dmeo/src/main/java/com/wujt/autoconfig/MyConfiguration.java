package com.wujt.autoconfig;

import com.wujt.factory.CarFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wujt  2021/6/1
 */
@Configuration
public class MyConfiguration {

    @Bean
    public CarFactoryBean carFactoryBean() {
        return new CarFactoryBean();
    }

}
