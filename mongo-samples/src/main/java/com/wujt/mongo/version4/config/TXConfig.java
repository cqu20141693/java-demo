package com.wujt.mongo.version4.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author wujt
 * @version 1.0
 * @date 2020/3/13
 */
@Configuration
public class TXConfig {
    @Bean
    PlatformTransactionManager transactionManager(MongoDbFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

}
