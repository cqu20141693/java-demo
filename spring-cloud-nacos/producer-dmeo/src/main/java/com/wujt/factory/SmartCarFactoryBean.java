package com.wujt.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.stereotype.Component;

/**
 * @author wujt  2021/6/1
 * 使用场景：
 * 创建复杂对象，或者需要计算得到的对象，或者需要读取配置后进行创建
 */
@Component
@Slf4j
public class SmartCarFactoryBean implements SmartFactoryBean<SmartCar> {
    @Override
    public SmartCar getObject() throws Exception {
        log.info("SmartCarFactoryBean getObject invoked");
        return new SmartCar();
    }

    @Override
    public Class<?> getObjectType() {
        return SmartCar.class;
    }

    /**
     * 对象是否为单例对象，单例对象会交给IOC容器管理
     *
     * @return boolean
     * @date 2021/6/1 15:56
     */
    @Override
    public boolean isSingleton() {
        return false;
    }

    /**
     * 是否提前初始化
     *
     * @return boolean
     * @date 2021/6/1 16:43
     */
    @Override
    public boolean isEagerInit() {
        return false;
    }

    @Override
    public boolean isPrototype() {
        return false;
    }
}
