package com.wujt.proxy;

import com.wujt.proxy.factory.CgLibProxyFactory;
import com.wujt.proxy.factory.ProxyFactory;

/**
 * 通过创建Enhancer 对象,
 * 然后指定代理基类：public void setSuperclass(Class superclass)
 * 然后指定MethodInterceptor（实现代理增强）：   public void setCallback(Callback callback)
 * 最后创建代理对象：    public Object create()
 *
 * @author wujt
 */
public class CglibProxyDemo {
    public static void main(String[] args) {

        //需要被代理的类
        HelloService helloService = new HelloServiceImpl();


        //CgLib代理
        ProxyFactory cgLibProxyFactory = new CgLibProxyFactory();
        HelloService cgLibProxy = cgLibProxyFactory.getProxy(helloService);
        cgLibProxy.echo("ricky");
        cgLibProxy.hashCode();

    }
}
