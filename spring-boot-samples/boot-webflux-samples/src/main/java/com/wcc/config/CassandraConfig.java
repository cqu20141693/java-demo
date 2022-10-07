package com.wcc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.ReactiveSessionFactory;
import org.springframework.data.cassandra.core.cql.ReactiveCqlTemplate;

/**
 * wcc 2022/9/27
 */
@Configuration
public class CassandraConfig {

    @Bean
    public ReactiveCqlTemplate reactiveCqlTemplate(ReactiveSessionFactory reactiveSessionFactory) {
        return new ReactiveCqlTemplate(reactiveSessionFactory);
    }
}
