package com.wujt.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartInitializingSingleton;

/**
 * @author wujt  2021/6/1
 */
@Slf4j
public class MyInitializingBean implements InitializingBean, SmartInitializingSingleton, DisposableBean {
    /**
     * bean 被注入容器,属性已经被设置后调用,功能类似于@Bean的init 配置
     *
     * @date 2021/6/2 9:54
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("MyInitializingBean afterPropertiesSet method invoked");
    }

    /**
     * Singleton Bean 被实例化之后会调用改方法，可用于执行启动逻辑
     *
     * @date 2021/6/2 9:57
     */
    @Override
    public void afterSingletonsInstantiated() {
        log.info("MyInitializingBean afterSingletonsInstantiated method invoked");
    }

    /**
     * 触发时机为当此对象销毁时，会自动执行这个方法
     *
     * @date 2021/6/2 14:22
     */
    @Override
    public void destroy() throws Exception {
        log.info("MyInitializingBean destroy method invoked");
    }
}
