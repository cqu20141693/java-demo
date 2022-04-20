### gateway auth
#### authenticate
1. 通过网关访问api需要提供认证headers:
``` 
Auth-Domain header :{@link com.gow.gateway.domian.auth.AuthDomain}

Auth Token header : Auth-Handler parse token for authentication
```
2. 自定义authFilter，进行header检查和检验
内部网关通过对header 头中的鉴权参数进行认证：
   identifier/token --> (userKey/passwrod,appKey/appToken,groupKey/groupToken,deviceKey/deviceToken)
#### authorize
2. 认证通过后需要进行权限校验（permission）

### log
1. 添加全局日志过滤器，设置其order为最小，记录LogInfo对象到请求属性中，可以在请求过程中修改日志信息，并将请求响应进行包装，
在回写响应前打印日志信息。主要记录请求时长，和自定义日志，包括路由信息，请求参数，鉴权参数等
   
### 异常处理
1. 主要是记录日志，统一处理异常为500返回

### 自定义header predicate
``` 
通过动态配置网关请求必须携带header，header是网关业务的主要设计模式，通过header标识实现api域划分，api的鉴权方式...
```
### 动态路由变更
``` 
通过nacosManager注册对应配置dataId的监听事件，当配置发生并更时，发送RefreshRoutesEvent，网关会清理缓存，重新拉取路由配置信息
```
### 负载均衡
``` 
通过webClient 配置负载均衡过滤器，在路由前会先查询微服务的实例，并选择其中的实例进行请求路由，默认不支持retry, 需要配置路由重试策略
```
### 限流器
``` 
spring cloud gateway 自实现了基于redis的限流过滤器，可以直接使用
```
### 熔断器