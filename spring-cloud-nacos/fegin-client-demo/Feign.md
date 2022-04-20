### feign use ribbon
1. LoadBalancerFeignClient  作为http client
2. LoadBalancerCommand 利用其submit  请求通过ILoadBalancer(ZoneAwareLoadBalancer) 获取服务列表
```java
public interface ILoadBalancer {
    public void addServers(List<Server> newServers);
    public Server chooseServer(Object key);
    public void markServerDown(Server server);
    public List<Server> getReachableServers();
    public List<Server> getAllServers();
}
```
```
http://springcloud.cn/view/35
主要的组件：
private ServerListFilter serverListFilter; 服务过滤
private IRule rule; 服务选择规则
private IPing ping = new DummyPing(); 服务检测ping，默认直接返回true
private ServerList serverListImpl; : 服务列表获取
private ServerListUpdater serverListUpdater; 服务列表更新

IRule: 用于选择server：
BestAvailableRule：选择最小请求数的Server
RandomRule： 随机选择
RoundRobinRule    线性轮询
RetryRule  根据轮询的方式重试
WeightedResponseTimeRule  根据响应时间去分配Weight，Weight越高，被选择的可能性就越大
ZoneAvoidanceRule 根据Server的zone区域和可用性来轮询选择

https://blog.csdn.net/lizc_lizc/article/details/106269569

ServerList用于获取服务实例列表和更新的服务实例列表: 具体实现NacosServiceList
public interface ServerList<T extends Server> {
    // 用于获取初始化的服务实例清单
    public List<T> getInitialListOfServers();
    
    // 用于获取更新的服务实例清单
    public List<T> getUpdatedListOfServers();   
}

ServerListUpdater是服 务 刷 新 更 新 器 、 启 动 定 时 任 务 、 负 责 定 时 从 注 册 中 心 获 取 更 新 的 服 务 列 表 ，
调 用com.netflix.loadbalancer.ServerList 实现更新，默认实现PollingServerListUpdater默认情况下，每隔30 秒拉取一次


```