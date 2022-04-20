package com.wujt.bean.processor;

import com.wujt.bean.MyInitializingBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author wujt  2021/6/2
 */
@Slf4j
public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof MyInitializingBean) {
            log.info("MyBeanPostProcessor postProcessBeforeInitialization method invoked bean={},name={}", bean, beanName);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof MyInitializingBean) {
            log.info("MyBeanPostProcessor postProcessAfterInitialization method invoked bean={},name={}", bean, beanName);
        }
        return bean;
    }
}
