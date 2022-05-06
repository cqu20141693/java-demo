## flink

### deploy

#### standalone

[集群standalone](https://blog.csdn.net/oMaFei/article/details/109575333)

[flink standalone](https://nightlies.apache.org/flink/flink-docs-release-1.14/docs/deployment/resource-providers/standalone/docker/)

##### docker

1. jobmanger

```
session 模式: 客户端提交任务，再调度任务
docker run --rm -d --name=jobmanager --network flink-network -p 8081:8081 -p 8088:8088 \
-e jobmanager.rpc.address: jobmanager flink:1.14.4-scala_2.11 jobmanager

application 模式: 启动时指定任务，只运行单一任务
https://nightlies.apache.org/flink/flink-docs-release-1.14/docs/deployment/resource-providers/standalone/docker/#app-cluster-yml
```

2. task manager

```
cluster:
docker run --rm -d --name=taskmanager --network flink-network --env JOB_MANAGER_RPC_ADDRESS=jobmanager flink:1.14.4-scala_2.11 taskmanager
```

##### 虚拟机

```

```

### flink 使用场景

### flink CDC
