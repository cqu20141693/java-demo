package com.gow.aop.aspectj.proxyfactory;

import java.lang.reflect.Method;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * @author gow
 * @date 2022/1/18
 */
@Aspect
@Configuration
@Slf4j
public class AspectArgusConfig {
    //定义method切入点，可以匹配LogService中所有方法
    @Pointcut("execution(* com.gow.aop.aspectj.service.LogService.*(..))")
    public void logPointcut() {

    }

    // 定义bean 类型切面
    @Pointcut("target(com.gow.aop.aspectj.service.LogService)")
    public void logTypePointcut() {

    }

    // 定义注解切面 类型切面
    @Pointcut("@annotation(com.gow.aop.aspectj.proxyfactory.RequireAop)")
    public void logAnnotationPointcut() {

    }

    //@3：定义了一个前置通知，这个通知对定义的切入点中的所有方法有效
    @Before(value = "logPointcut()")
    public void beforeAdvice(JoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SpelExpressionParser parser = new SpelExpressionParser();
        String userId = parser.parseExpression("#{#userId}", new TemplateParserContext()).getValue(
                createEvaluationContext(method, args, target), String.class);
        Set<String> roles = parser.parseExpression("#{#info.roles}", new TemplateParserContext()).getValue(
                createEvaluationContext(method, args, target), Set.class);
        Long time = parser.parseExpression("#{#info.time}", new TemplateParserContext()).getValue(
                createEvaluationContext(method, args, target), Long.class);

        log.info("{} {} {}", userId, roles, time);
        //输出连接点的信息
        System.out.println("beforeAdvice，" + joinPoint);

    }

    /**
     * 构造SpEL表达式上下文
     */
    private EvaluationContext createEvaluationContext(Method method, Object[] args, Object target) {
        return new MethodBasedEvaluationContext(
                target, method, args, new DefaultParameterNameDiscoverer());
    }

    @After("logPointcut()")
    public void afterAdvice(JoinPoint joinPoint) {
        System.out.println("afterAdvice..." + joinPoint);
    }

    @Around("logPointcut()")
    public void aroundAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        Object target = proceedingJoinPoint.getTarget();
        Object[] args = proceedingJoinPoint.getArgs();
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();

        System.out.println("aroundAdvice before," + proceedingJoinPoint);

        try {
            proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        System.out.println("aroundAdvice after," + proceedingJoinPoint);
    }
}
