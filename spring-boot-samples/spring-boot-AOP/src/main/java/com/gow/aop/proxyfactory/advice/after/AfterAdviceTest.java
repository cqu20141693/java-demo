package com.gow.aop.proxyfactory.advice.after;

import com.gow.aop.proxyfactory.advice.FundsService;
import java.lang.reflect.Method;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.framework.ProxyFactory;

/**
 * @author gow
 * @date 2021/7/22 0022
 */
public class AfterAdviceTest {
    public static void main(String[] args) {
        //代理工厂
        ProxyFactory proxyFactory = new ProxyFactory(new FundsService());
        //添加一个方法前置通知，判断用户名不是“路人”的时候，抛出非法访问异常
        proxyFactory.addAdvice(new AfterReturningAdvice() {
            @Override
            public void afterReturning(Object returnValue, Method method, Object[] args, @Nullable Object target)
                    throws Throwable {
                System.out.println(String.format("method=%s return %s",method.getName(),returnValue.toString()));
            }
        });
        //通过代理工厂创建代理
        FundsService proxy = (FundsService) proxyFactory.getProxy();
        //调用代理的方法
        proxy.recharge("路人", 100);
        proxy.getBalance("路人");
    }
}
