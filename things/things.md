## 物联网

###         

### device

### EventBus

```markdown
 订阅者


1. 通过EventBus订阅api进行发起订阅
2. 通过TopicSubscribe 注解进行订阅
---
 订阅信息

1. 订阅者id ： 订阅者唯一标识
2. 订阅topic：支持模糊匹配
3. 订阅特征：
4. 订阅消息类型： 消息codec

---
 订阅特征

本地订阅: 直接订阅到本地broker
集群订阅: 通过本地broker 通知集群其他broker进行集群订阅
共享订阅： 当一个消息被多个订阅者订阅时，只发送给一个订阅者
---

 broker 
 
提供消息队列，进行消息事件驱动：

---
可以利用响应式流进行数据的发布订阅
可以利用本地队列实现
---

 本地订阅实现

 事件驱动：
订阅者订阅topic, 本地构建订阅树和订阅信息
生产者生产消息，通过订阅数match 订阅信息，通过订阅信息将消息发送到订阅者处理
---

 共享订阅

当订阅树中存在多个匹配时，只分发给一个订阅者，轮询
---

集群订阅

    以redis作为消息队列
1. 首先本地broker构建一个redis的订阅Consumer，订阅stream数据流,事件到达后路由到本地订阅："/broker/bus/" + localId + "/" + brokerId
2. 本地broker 发送一个订阅事件到集群："/broker/" + brokerId + "/" + localId + "/subs"
3. 本地broker将集群订阅事件放到redis Set中(用于新增节点读取构建本地订阅)： "/broker/" + localId + "/" + brokerId + "/subs"
4. 本地节点做订阅集群订阅事件，构建本地订阅到订阅树，订阅信息的中的订阅者为redis 生产者，消息发送到1中消费："/broker/bus/" + localId + "/" + brokerId
5. 本地broker启动时读取集群订阅redis Set中的订阅，构建4中的订阅树
---
```
