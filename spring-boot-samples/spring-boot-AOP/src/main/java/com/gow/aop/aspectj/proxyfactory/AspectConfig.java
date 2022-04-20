package com.gow.aop.aspectj.proxyfactory;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

/**
 * @author gow
 * @date 2021/7/26 0026
 */
// 切面(Advisor)
@Configuration
@Aspect
public class AspectConfig {
    //@2：定义了一个切入点，可以匹配CommonService中所有方法
    @Pointcut("execution(* com.gow.aop.aspectj.service.CommonService.*(..))")
    public void logPointcut() {

    }

    //@3：定义了一个前置通知，这个通知对定义的切入点中的所有方法有效
    @Before(value = "logPointcut()")
    public void beforeAdvice(JoinPoint joinPoint) {
        //输出连接点的信息
        System.out.println("beforeAdvice，" + joinPoint);
    }

    @After("logPointcut()")
    public void afterAdvice(JoinPoint joinPoint) {
        System.out.println("afterAdvice..." + joinPoint);
    }

    @Around("logPointcut()")
    public void aroundAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        System.out.println("aroundAdvice before," + proceedingJoinPoint);

        try {
            proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        System.out.println("aroundAdvice after," + proceedingJoinPoint);
    }


    //@4：定义了一个异常通知，这个通知对定义的切入点中的所有方法有效
    @AfterThrowing(value = "logPointcut()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e) {
        //发生异常之后输出异常信息
        System.out.println(joinPoint + ",发生异常：" + e.getMessage());
    }
}
