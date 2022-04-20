package com.wujt.autoconfig;

import com.wujt.autoconfig.register.MyImportBeanDefinitionRegistrar;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author wujt  2021/6/1
 */
@Configuration
@Import({ MyImportBeanDefinitionRegistrar.class})
@ConditionalOnProperty(prefix = "gow.autoconfig",name = "enable", havingValue = "registrar")
public class MyRegistrarConfiguration {

}
