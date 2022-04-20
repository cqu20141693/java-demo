package com.gow.device.operation.config;

import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class LinkDbConfig {
    @Autowired
    @Qualifier("linkDb")
    private DataSource linkDbDataSource;

    @Bean(name = {"linkSqlSessionFactory", "userSqlSessionFactory"})
    public SqlSessionFactory linkSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(linkDbDataSource);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:mybatis/mappers/*.xml"));
        factoryBean.setConfigLocation(new PathMatchingResourcePatternResolver()
                .getResource("classpath:mybatis/mybatis-config.xml"));
        return factoryBean.getObject();
    }

    @Bean(name = "linkTransactionManager")
    public PlatformTransactionManager linkTransactionManager(@Autowired @Qualifier("linkDb") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "linkTransactionTemplate")
    public TransactionTemplate transactionTemplate(@Autowired @Qualifier("linkTransactionManager") PlatformTransactionManager platformTransactionManager){
        return new TransactionTemplate(platformTransactionManager);
    }

    @Bean(name = "linkSqlSessionTemplate")
    public SqlSessionTemplate linkSqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(linkSqlSessionFactory());
    }
}
