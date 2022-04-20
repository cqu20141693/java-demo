### spring
####  BeanFactory 和ApplicationContext
1. BeanFactory 是容器的抽象定义，其主要是对IOC容器能力的定义，ApplicationContext是对BeanFactory的扩展，
ApplicationContext 持有BeanFactory对象，并且实现了ResourceLoader，MessageResource，ApplicationEventPublisher
#### @ Import 和 Enable注解实现bean注入
1. Enable注解一般都使用Import注解实现
2. Import注有三种方式注入bean：添加配置类，实现ImportBeanDefinitionRegistrar，实现ImportSelector三种方式实现bean注入

#### @ Configuration，Conditional 系列注解
1.  前者为java bean配置文件,后者为条件注解

#### FactoryBean 的使用场景
1. 当创建一个对象比较复杂或者依赖外部配置时，可以使用其创建
2. 可以创建非扫描包下的对象，当然也可以利用@Bean注入
3. [mybatis应用](https://juejin.cn/post/6844903954615107597)

#### ApplicationEvent 发布和监听
1. 通过添加监听事件到上下文，ApplicationContext 发布事件

#### BeanFactoryPostProcessor
1. ConfigurationClassPostProcessor 类的postProcessorBeanFactory()方法进行了@ComponentScan的扫描
@Component注解的解析，，以及@Import注解的处理
#### BeanPostProcessor
1. ConfigurationPropertiesBindingPostProcessor 处理ConfigurationProperties 注解
2. AutowiredAnnotationBeanPostProcessor 处理Autowired，Inject和Value注解
3. CommonAnnotationBeanPostProcessor处理Resource，PostConstruct，PreDestroy注解
4. EventListenerMethodProcessor 处理EventListener 注解注入事件监听
5. ApplicationContextAwareProcessor 提供了Aware实例的注入

#### SmartLifecycle
1. 当容器创建完成后会回调start 方法
2. 当容器关闭时会回调stop 方法


#### spring 问题
1. spring 如何解决循环依赖的？
``` 
spirng 不能解决Autowired 构造器的循环依赖
spring在构建实例时分为了两个步骤：实例化（instant），初始化(initialize);
当spring实例化对象后，如果 BeanDefinition 的 earlySingletonExpore=true，
将Bean 存在singletonFactories缓存中，如果发现有其他使用地方，则将其移动到earlySingletonObjects
最终添加到singletonObjects，就能被使用

https://mp.weixin.qq.com/s/FtbzTMxHgzL0G1R2pSlh-A
```

#### 参考
1. [Spring Ioc容器设计与实现](https://www.cnblogs.com/hello-shf/p/11006750.html)
2. [spring 解决循环依赖](https://mp.weixin.qq.com/s/FtbzTMxHgzL0G1R2pSlh-A)
3. [Spring bean创建原理](https://mp.weixin.qq.com/s/WwjicbYtcjRNDgj2bRuOoQ)
3. [Spring SPI原理](https://mp.weixin.qq.com/s/WwjicbYtcjRNDgj2bRuOoQ)