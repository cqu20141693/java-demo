### pulsar core

#### functions

1. StringProducerTemplate

``` 
Strater 自动配置StringProducerTemplate bean
提供提前配置producer
也支持发送数据创建producer

同时实现DisposableBean 实现优雅的退出关闭Producer
```

2. consumer

``` 
@EnablePulsar 注解开启对@PulsarSubscribe注解的支持

支持通过@PulsarSubscribe 配置consumer（推荐String）
支持三种消费方式：
receive(Consumer<T> consumer, Message<T> msg) ： 该消费需要用户自己进行消息ack
receive(Message<T> msg) 和receive(T msg)  : 两种消费模式支持配置容器AckStrategy,默认AckCountTimeStrategy

container 支持Spirng SmartLifecycle 进行容器生命周期的管理，实现consumer 优雅关闭
container 支持对consumer AckStrategy
AckCountTimeStrategy： 消息到达即确认(由container 实现定时和count累积ACK)，数据以吞吐量为目的，存在数据部分重复消费
DefaultAckStrategy： 消息到达即确认(每条消息都单独确认)，保证消息的消费，但是无法使用累积ACK

container 支持ApplicationEventPublisher，容器关闭时发送PulsarContainerStopEvent
```

3. manager

``` 
DefaultPulsarManager 实现对pulsar tenant,namespace,topic,resrouce，token等的管理

```

4. config

``` 
PulsarJwtConfig ： 配置jwttoken
PulsarProperties : 配置pulsar client,consuemr,producer
ContainerProperties : 配置PulsarContainer

```

### MQ 深入理解

#### 为什么使用Pulsar

``` 

```

#### 如何保证消息至少消费一次

1.保证生产者幂等性

``` 
生产者唯一id + 消息序列号 + broker 端去重配置

```

2. 保证消费者 消费后进行数据确认

``` 
consuemr msg
ack msg
```

#### 如何保证消息消费幂等性

``` 

```

#### 如何处理消费者异常

``` 
死信队列

```

#### 如何保证消息的顺序性

``` 

```

#### 如何解决消息队列的延时以及过期失效问题？消息队列满了以后该怎么处理？有几百万消息持续积压几小时，说说怎么解决？

``` 
pulsar 延迟消息只支持shared 订阅模式
```