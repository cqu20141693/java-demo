package com.wujt.bean.processor;

import com.wujt.bean.MyInitializingBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;

import java.beans.PropertyDescriptor;

/**
 * 参考 AutowiredAnnotationBeanPostProcessor
 *
 * @author wujt  2021/6/2
 */
@Slf4j
public class MyInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {


    //InstantiationAwareBeanPostProcessor interface

    /**
     * 实例化前置处理,spring aop 通过这里进行扩展实现
     * 返回对象不为空时，调用postProcessAfterInstantiation方法后直接返回bean,否则继续执行后续操作
     *
     * @param beanClass
     * @param beanName
     * @return java.lang.Object
     * @date 2021/6/2 10:03
     */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if (beanClass == MyInitializingBean.class) {
            log.info("MyInstantiationAwareBeanPostProcessor postProcessBeforeInstantiation method invoked class={},beanName={}", beanClass, beanName);
        }
        return null;
    }

    /**
     * 实例化后置处理
     *
     * @param bean
     * @param beanName
     * @return boolean
     * @date 2021/6/2 10:03
     */
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if (bean instanceof MyInitializingBean) {
            log.info("MyInstantiationAwareBeanPostProcessor postProcessAfterInstantiation method invoked bean={},name={}", bean, beanName);
        }
        return true;
    }

    /**
     * @param pvs
     * @param bean
     * @param beanName
     * @return org.springframework.beans.PropertyValues
     * @date 2021/6/2 10:05
     */
    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        if (bean instanceof MyInitializingBean) {
            log.info("MyInstantiationAwareBeanPostProcessor postProcessProperties method invoked,pvs={},bean={},name={]", pvs, bean, beanName);
        }
        return pvs;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
        return postProcessProperties(pvs, bean, beanName);
    }

    // BeanPostProcessor interfaces


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
