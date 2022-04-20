package com.wujt.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author wujt
 * @version 1.0
 * @date 2020/3/23
 */
@Configuration
public class RabbitMqConfig {


    @Bean
    public ConnectionFactory myConnectionFactory(RabbitMqInfo config) {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(config.getHost());
        factory.setPort(config.getPort());
        factory.setUsername(config.getUsername());
        factory.setPassword(config.getPassword());
        factory.setVirtualHost(config.getVirtualHost());
        // 设置回调
        factory.setPublisherConfirms(config.getPublisherConfirms());
        return factory;
    }


    @Bean
    public SimpleRabbitListenerContainerFactory myFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            ConnectionFactory myConnectionFactory,
            MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, myConnectionFactory);
        factory.setMessageConverter(messageConverter());
        return factory;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new RabbitMqFastJsonConverter();

//        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
//        converter.setClassMapper(new MYClassMapper());
//        return converter;
    }

    @Bean
    /** 因为要设置回调类，所以应是prototype类型，如果是singleton类型，则回调类为最后一次设置 */
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate(RabbitMqInfo config) {
        RabbitTemplate template = new RabbitTemplate(myConnectionFactory(config));
        template.setMessageConverter(messageConverter());
        return template;
    }

}
