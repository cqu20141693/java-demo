package com.gow.config;

import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {
    @Bean(name = "linkDb")
    @ConfigurationProperties(prefix = "spring.datasource.link")
    public DataSource linkDbDataSource() {
        return DataSourceBuilder.create().build();
    }
}
