package com.gow.aop.proxyfactory;

import com.gow.aop.proxyfactory.advice.FundsService;
import java.lang.reflect.Method;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.lang.Nullable;

/**
 * @author gow
 * @date 2021/7/22 0022
 */
public class CGLibProxyFactoryDemo {
    public static void main(String[] args) {
        ProxyFactory proxyFactory = new ProxyFactory();
        // 定义切入点
        proxyFactory.setTarget(new FundsService());
        // 定义通知
        proxyFactory.addAdvisor(new DefaultPointcutAdvisor(new MethodBeforeAdvice() {
            @Override
            public void before(Method method, Object[] args, @Nullable Object target) throws Throwable {
                System.out.println(method);
            }
        }));
        //创建代理对象
        Object proxy = proxyFactory.getProxy();
        System.out.println("代理对象的类型：" + proxy.getClass());
        System.out.println("代理对象的父类：" + proxy.getClass().getSuperclass());
        System.out.println("代理对象实现的接口列表");
        for (Class<?> cf : proxy.getClass().getInterfaces()) {
            System.out.println(cf);
        }
    }
}
