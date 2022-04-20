package com.wujt.event;

import com.wujt.bean.factory.MyBeanDefinitionRegistryPostProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author wujt  2021/6/1
 */
@Slf4j
public class MyApplicationContextInitializer implements ApplicationContextInitializer {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        log.info("MyApplicationContextInitializer initialize method invoked applicationContext={}", applicationContext);
        applicationContext.addBeanFactoryPostProcessor(new MyBeanDefinitionRegistryPostProcessor());
    }
}
