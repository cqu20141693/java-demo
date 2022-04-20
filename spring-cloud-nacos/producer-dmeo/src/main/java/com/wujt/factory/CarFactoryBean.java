package com.wujt.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author wujt  2021/6/1
 * 使用场景：
 * 创建复杂对象，或者需要计算得到的对象，或者需要读取配置后进行创建
 */
@Slf4j
public class CarFactoryBean implements FactoryBean<Car> {
    @Override
    public Car getObject() throws Exception {
        log.info("CarFactoryBean getObject invoked");
        return new Car();
    }

    @Override
    public Class<?> getObjectType() {
        return Car.class;
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
}
