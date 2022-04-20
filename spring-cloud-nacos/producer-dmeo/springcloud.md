### spring cloud
#### BootstrapConfiguration
1. 导入了spring cloud PropertySourcePostProcessor

#### NacosConfigBootstrapConfiguration
1. nacos 配置自动启动


#### nocos config
1. 配置文件优先级顺序
```
bootstrap.*里面的配置 >链接Config server，加载远程配置(git仓库等) >加载application.*里面的配置
``` 
2. 配置读取和刷新原理
```


Spring Cloud Alibaba Nacos Config 目前提供了三种配置能力从 Nacos 拉取相关的配置。
A: 通过 spring.cloud.nacos.config.shared-configs[n].data-id 支持多个共享 Data Id 的配置。
B: 通过 spring.cloud.nacos.config.extension-configs[n].data-id 的方式支持多个扩展Data Id 的配置。
C: 通过内部相关规则(应用名、应用名+ Profile )自动生成相关的 Data Id 配置。
当三种方式共同使用时，他们的一个优先级关系是:A < B < C。

实现原理：集成Spring Environment，负责管理spring的运行相关的配置信息，针对不同类型的配置都会有与之对应的PropertySource，
比如（SystemEnvironmentPropertySource、CommandLinePropertySource）。以及PropertySourcesPropertyResolver来进行解析
通过　NacosPropertySourceLocator 加载配置 顺序为C,B,A

Nacos Config 默认支持配置的动态更新，扩展配置默认是不支持，默认情况下所有共享配置的 Data Id 都不支持动态刷新。
然后开启配置动态刷新:NacosConfigService 中的ClientWorker 开始定时任务
客户端发起长轮训请求，
服务端收到请求以后，先比较服务端缓存中的数据是否相同，如果不同，则直接返回
如果相同，则通过schedule延迟29.5s之后再执行比较
为了保证当服务端在29.5s之内发生数据变化能够及时通知给客户端，服务端采用事件订阅的方式来监听服务端本地数据变化的事件，一旦收到事件，则触发DataChangeTask的通知，并且遍历allStubs队列中的ClientLongPolling,把结果写回到客户端，就完成了一次数据的推送
如果 DataChangeTask 任务完成了数据的 “推送” 之后，ClientLongPolling 中的调度任务又开始执行了怎么办呢？很简单，只要在进行 “推送” 操作之前，先将原来等待执行的调度任务取消掉就可以了，这样就防止了推送操作写完响应数据之后，调度任务又去写响应数据，这时肯定会报错的。所以，在ClientLongPolling方法中，最开始的一个步骤就是删除订阅事件
所以总的来说，Nacos采用推+拉的形式，来解决最开始关于长轮训时间间隔的问题。当然，30s这个时间是可以设置的，而之所以定30s，应该是一个经验值。
https://www.cnblogs.com/hankuikui/p/12084193.html
之后通过spring Event通知机制，更新环境并重新绑定数据到bean

```
3. [配置读取](https://www.cnblogs.com/wuzhenzhao/p/11385079.html) 和 [动态刷新](https://www.cnblogs.com/hankuikui/p/12084193.html)
