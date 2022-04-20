package com.wujt.proxy;

import com.wujt.proxy.factory.JdkProxyFactory;
import com.wujt.proxy.factory.ProxyFactory;

/**
 * 需要实现 InvocationHandler，在handler中进行增强实现
 * 然后调用 Proxy类  public static Object newProxyInstance(ClassLoader loader,  // 直接获取应用ClassLoader
 *                                           Class<?>[] interfaces,  // 直接获取类的接口
 *                                           InvocationHandler h)  // invocationHandler
 *
 * @author wujt
 */
public class JDKProxyDemo {
    public static void main(String[] args) {

        //需要被代理的类
        HelloService helloService = new HelloServiceImpl();
        //jdk代理
        ProxyFactory jdkProxyFactory = new JdkProxyFactory();
        HelloService jdkProxy = jdkProxyFactory.getProxy(helloService);
        jdkProxy.echo("ricky");
        jdkProxy.hashCode();
    }
}
