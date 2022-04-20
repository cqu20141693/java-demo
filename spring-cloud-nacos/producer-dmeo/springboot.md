### spring boot 
#### SpringApplication

#### spring.factories 之 EnableAutoConfiguration

#### spring.factories 之 ApplicationContextInitializer
1. 在容器刷新前（创建上下文后）调用此类的initialize方法；场景可能为，在最开始激活一些配置，比如Environment配置
或者利用这时候class还没被类加载器加载的时机，进行动态字节码注入等操作。
重要初始化器：PropertySourceBootstrapConfiguration
#### spring.factories 之 ApplicationListener
1. spring boot启动时的监听器，可以监听spring boot启动过程中发布的ApplicationEvent，并做对应的处理，
重要的监听器有：EnvironmentPostProcessorApplicationListener

#### @SpringBootApplication


#### 参考
1. [spring boot 配置文件动态更新原理](https://www.cnblogs.com/hankuikui/p/12084193.html)
1. [spring boot 扩展接口](https://zhuanlan.zhihu.com/p/266126121)
