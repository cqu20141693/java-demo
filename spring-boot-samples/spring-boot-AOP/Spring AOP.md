### spring AOP

``` 
spring中创建代理主要分为2种：
手动方式和自动化的方式 手动通过 ProxyFactory 创建 
自动通过 ProxyFactoryBean 进行配置
还可以通过AspectJ 进行自动配置

```

#### spring boot

``` 
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-aop</artifactId>
    </dependency>
    
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
    public void before(JoinPoint joinPoint) {
        //输出连接点的信息
        System.out.println("前置通知，" + joinPoint);
    }

    //@4：定义了一个异常通知，这个通知对定义的切入点中的所有方法有效
    @AfterThrowing(value = "logPointcut()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e) {
        //发生异常之后输出异常信息
        System.out.println(joinPoint + ",发生异常：" + e.getMessage());
    }
}

```

##### AopAutoConfiguration

``` 
@EnableAspectJAutoProxy : 通过注入AnnotationAwareAspectJAutoProxyCreator

```

##### AnnotationAwareAspectJAutoProxyCreator

``` 
实现了SmartInstantiationAwareBeanPostProcessor  
在bean 实例化之前对类进行AspectJ anntation 扫描，生成代理对象
@Aspect
 
 ```

##### annotation

1. 切面： @AspectJ
2. 切入点： @Pointcut

``` 
表达式标签（10种）
execution：用于匹配方法执行的连接点
within：用于匹配指定类型内的方法执行
this：用于匹配当前AOP代理对象类型的执行方法；注意是AOP代理对象的类型匹配，这样就可能包括引入接口也类型匹配
target：用于匹配当前目标对象类型的执行方法；注意是目标对象的类型匹配，这样就不包括引入接口也类型匹配
args：用于匹配当前执行的方法传入的参数为指定类型的执行方法
@within：用于匹配所以持有指定注解类型内的方法
@target：用于匹配当前目标对象类型的执行方法，其中目标对象持有指定的注解
@args：用于匹配当前执行的方法传入的参数持有指定注解的执行
@annotation：用于匹配当前执行方法持有指定注解的方法
bean：Spring AOP扩展的，AspectJ没有对于指示符，用于匹配特定名称的Bean对象的执行方法
```

3. 通知：

``` 
@Around: AspectJAroundAdvice
@Before: AspectJMethodBeforeAdvice
@After: AspectJAfterAdvice
@AfterReturning: AspectJAfterReturningAdvice
@AfterThrowing: AspectJAfterThrowingAdvice
```

#### JoinPoint

连接点（JoinPoint）

#### Advice

``` 
通知（Advice）用来指定需要增强的逻辑
```

##### Interceptor

1. MethodInterceptor

``` 
方法拦截器，所有的通知(Advice)均需要转换为MethodInterceptor类型的，
最终多个MethodInterceptor组成一个方法拦截器连。
```

#### PointCut

``` 
切入点(PointCut) 用来指定哪些类的哪些方法中需要使用通知（Advice）增强逻辑
```

#### Advisor

``` 
切面(Advisor) 组合Advice和Point ：
1： 需要增强的目标方法列表，这个通过切入点(Pointcut)来指定
2： 需要在目标方法中增强的逻辑，这个通过(Advice)通知来指定
```

#### AopProxyFactory and AdvisedSupport

1. DefaultAopProxyFactory
2. ProxyFactory extends ProxyCreatorSupport

``` 
ProxyCreatorSupport用来对代理的创建提供支持，内部添加了AopProxyFactory对象的引用
```

### AspectJ

#### AspectJProxyFactory

``` 
@Aspect标注的类上: 表示定义aop
@Pointcut来定义切入点: 
@Before、@Around、@After、@AfterRunning、@AfterThrowing标注在方法上来定义通知，
定义好了之后，将@Aspect标注的这个类交给AspectJProxyFactory来解析生成Advisor链
```

### 参考

1. [spring aop](http://www.itsoku.com/article/297#menu_25)
2. [aop 原理](https://blog.csdn.net/anLA_/article/details/105670360)
3. [aop 源码](https://github.com/doocs/source-code-hunter/blob/main/docs/Spring/AOP/AOP%E6%BA%90%E7%A0%81%E5%AE%9E%E7%8E%B0%E5%8F%8A%E5%88%86%E6%9E%90.md)

