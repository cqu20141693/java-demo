package com.gow.aop.proxyfactory;

import com.gow.aop.proxyfactory.join_point.IService;
import com.gow.aop.proxyfactory.join_point.Service;
import java.lang.reflect.Method;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.lang.Nullable;

/**
 * @author gow
 * @date 2021/7/22 0022
 */
public class JDKProxyFactoryDemo {
    public static void main(String[] args) {
        Service target = new Service();

        ProxyFactory proxyFactory = new ProxyFactory();
        //设置需要被代理的对象
        proxyFactory.setTarget(target);
        //设置需要代理的接口: 切入点
        proxyFactory.addInterface(IService.class);
        // 定义通知
        proxyFactory.addAdvice(new MethodBeforeAdvice() {
            @Override
            public void before(Method method, Object[] args, @Nullable Object target) throws Throwable {
                System.out.println(method);
            }
        });

        IService proxy = (IService) proxyFactory.getProxy();
        System.out.println("代理对象的类型：" + proxy.getClass());
        System.out.println("代理对象的父类：" + proxy.getClass().getSuperclass());
        System.out.println("代理对象实现的接口列表");
        for (Class<?> cf : proxy.getClass().getInterfaces()) {
            System.out.println(cf);
        }
        //调用代理的方法
        System.out.println("\n调用代理的方法");
        proxy.say("spring aop");
    }
}
