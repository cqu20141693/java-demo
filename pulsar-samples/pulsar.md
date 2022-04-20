### pulsar

#### consumer

1. api

``` 
Command Subscribe
Command Flow
receive,batchReceive message
Command Ack
Command CloseConsumer
Command RedeliverUnacknowledgedMessages
Command ReachedEndOfTopic
Command ConsumerStats
Command ConsumerStatsResponse
Command Unsubscribe

reconsumeLater

```

#### Resource

1. tenant

``` 
一类使用者： 比如一个部门，一个群体
```

2. namespace

``` 
一种业务
```

3. topic

``` 
一个具体的数据结构
```