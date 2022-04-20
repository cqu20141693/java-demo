### disruptor

#### 调优参数

1. single vs multiple producer

``` 
单生产者性能高于多生产者，避免了预分配
```

#### WaitStrategy

1. Disruptor 提供了多个 WaitStrategy（等待策略）的实现

``` 
每种策略都具有不同性能和优缺点，根据实际运行环境的 CPU 的硬件特点选择恰当的策略，并配合特定的 JVM 的配置参数，能够实现不同的性能提升
BlockingWaitStrategy 是最低效的策略，但其对 CPU 的消耗最小并且在各种不同部署环境中能提供更加一致的性能表现；
SleepingWaitStrategy 的性能表现跟 BlockingWaitStrategy 差不多，对 CPU 的消耗也类似，但其对生产者线程的影响最小，适合用于异步日志类似的场景；
YieldingWaitStrategy 适合用于低延迟的系统。在要求极高性能且事件处理线数小于 CPU 逻辑核心数的场景中，推荐使用此策略；例如：CPU 开启超线程的特性。
BusySpinWaitStrategy：是性能最高的等待策略，但对部署环境的约束也最高。只有当事件处理程序线程的数量小于机器上的物理核的数量时使用，超线程技术禁用
```

#### 消費者

##### 点对点消费（WorkHandler）

##### 发布订阅（EventHandler）

##### 顺序处理规则

1. then 方法

#### 参考

1. https://tech.meituan.com/2016/11/18/disruptor.html
2. https://www.hangge.com/blog/cache/detail_2851.html
3. https://lmax-exchange.github.io/disruptor/user-guide/index.html