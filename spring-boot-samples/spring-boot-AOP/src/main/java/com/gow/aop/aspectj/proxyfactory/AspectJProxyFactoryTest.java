package com.gow.aop.aspectj.proxyfactory;

import com.gow.aop.aspectj.service.CommonService;
import com.gow.aop.aspectj.service.CommonServiceImpl;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

/**
 * @author gow
 * @date 2021/7/26 0026
 */
public class AspectJProxyFactoryTest {
    public static void main(String[] args) {
        try {
            //对应目标对象
            CommonService target = new CommonServiceImpl();
            //创建AspectJProxyFactory对象
            AspectJProxyFactory proxyFactory = new AspectJProxyFactory();
            //设置被代理的目标对象
            proxyFactory.setTarget(target);
            //设置标注了@Aspect注解的类
            proxyFactory.addAspect(AspectConfig.class);
            //生成代理对象
            CommonService proxy = proxyFactory.getProxy();
            //使用代理对象
            proxy.greeting();
            proxy.welcome();
            proxy.exception();
        } catch (Exception e) {
        }
    }
}
