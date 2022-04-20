package com.wujt.bean.factory;

import com.wujt.bean.MyInitializingBean;
import com.wujt.bean.processor.MyBeanPostProcessor;
import com.wujt.bean.processor.MyInstantiationAwareBeanPostProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

/**
 * @author wujt  2021/6/1
 */
@Slf4j
public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    /**
     * 可用于加载自定义的BeanDefinition :ConfigurationClassPostProcessor
     *
     * @param registry
     * @date 2021/6/2 9:40
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        log.info("MyBeanDefinitionRegistryPostProcessor postProcessBeanDefinitionRegistry method invoked registry={}", registry);
        AnnotatedGenericBeanDefinition annotatedGenericBeanDefinition = new AnnotatedGenericBeanDefinition(MyInitializingBean.class);
        registry.registerBeanDefinition(MyInitializingBean.class.getName(), annotatedGenericBeanDefinition);
        log.info("postProcessBeanDefinitionRegistry register bean={}", MyInitializingBean.class);
    }

    /**
     * 调用时机在spring在读取beanDefinition信息之后，实例化bean之前:
     * 可以加载一些BeanPostProcessor处理bean,或者处理BeanDefinition的元信息
     *
     * @param beanFactory
     * @date 2021/6/2 9:41
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.info("MyBeanDefinitionRegistryPostProcessor postProcessBeanFactory method invoked beanFactory={}", beanFactory);

        beanFactory.addBeanPostProcessor(new MyInstantiationAwareBeanPostProcessor());
        beanFactory.addBeanPostProcessor(new MyBeanPostProcessor());
    }
}
