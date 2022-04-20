### sentinel

1. 熔断能力

``` 
https://github.com/alibaba/Sentinel/blob/master/sentinel-demo/sentinel-demo-basic/src/main/java/com/alibaba/csp/sentinel/demo/degrade/ExceptionRatioCircuitBreakerDemo.java
https://github.com/alibaba/Sentinel/wiki/%E7%86%94%E6%96%AD%E9%99%8D%E7%BA%A7
https://spring-cloud-alibaba-group.github.io/github-pages/hoxton/zh-cn/index.html#_zuul_%E6%94%AF%E6%8C%81

根据慢调用比例熔断： 需要配置资源，统计时间，RT响应时间阈值，比例阈值，最小的请求数
根据异常比例熔断：配置需要统计的时间，异常比例值，熔断时长，最小请求数量；经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求成功完成（没有错误）则结束熔断，否则会再次被熔断
根据异常数熔断：配置异常数和统计时间配置熔断时长；
```

#### Feign 集成

1. maven

```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>

```

2. feign.sentinel.enabled=true
3. 配置降级策略

``` 
默认对请求数不大的使用慢调用比例且相对时间比较长的， 配置时长为30S,最小调用3次，慢调用时长为3S,慢调用比例为30%,熔断窗口10S
对与一些请求比较频繁的，使用异常比例进行配置：配置统计时长为10S,调用次数5，异常比例为20%，熔断窗口为30S,

开发一个扫描feignClient的模块，对资源进行配置即可实现熔断规则,
可以通过dashboard进行配置开发
```

#### web 服务端集成

1. maven

``` 
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
</dependency>
```

2. 使用SentinelResource 注解用来标识资源是否被限流、降级

3. 配置降级和限流规则

``` 
开发一个扫描SentinelResource的模块，对资源进行配置即可实现熔断规则,
```

#### 集成dashboard

1. dashboard功能
2. 集成动态数据源
3. 二次开发动态刷新

#### 参考

1. [sentinel 限流](https://github.com/doocs/source-code-hunter/tree/main/docs/Sentinel)