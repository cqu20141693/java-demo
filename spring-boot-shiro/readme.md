## shiro

### ShiroFilterFactoryBean 和SecurityManager
1. ShiroFilterFactoryBean 中添加拦截器和SecurityManager
2. SecurityManager 中配置cacheManager,Realm,SessionManager
3. securityManager 管理者所有的Subject
### Subject
1. 代表了当前用户，携带身份认证信息进行login请求

### realm
1. AuthorizingRealm
2. AuthenticatingRealm
### 拦截器
anon：匿名拦截器，即不需要登录即可访问；一般用于静态资源过滤；示例“/static/**=anon”

authc：认证拦截器，用户需要认证，如果没有认证，将会重定向到的登录页面。

roles：角色授权拦截器，验证用户是否拥有所有角色；示例   /actions/**=roles[admin]，发送任何以actions开头的请求，都需  要admin角色。

perms：权限授权拦截器，验证用户是否拥有所有权限；示例   /user/**=perms["user:create"]”

logout：退出拦截器，主要属性：redirectUrl：退出成功后重定向的地址。示例  /logout=logout

port：示例“/test= port[80]”，如果用户访问该页面是非80，将自动将请求端口改为80并重定向到该80端口

ssl ：SSL拦截器，只有请求协议是https才能通过；否则自动跳转会https端口（443）；其他和port拦截器一样；

### Authenticator 和AuthenticationStrategy
### Authorizer
### 注解和AnnotationsAuthorizingMethodInterceptor


#### 功能介绍
1. 注册用户： 为用户生成密码和盐值以及userKey,userName
2. 登录用户：利用UserPasswordRealm 对UsernamePasswordToken的登录请求进行身份认证，
认证后生成Cookie和RefreshCookie,并返回用户信息
3. token验证，当请求接口时需要进行token验证时，首先是校验cookie，通过则访问，而后校验refreshCookie，


### 流程图
1. 认证流程图：
首先是通过SecurityUtil.getSubject()获取当前请求用户，然后创建AuthenticationToken，调用Subject.login(token);
其次会进入调用内部组装的SecurityManager进行login(subject,token)操作,再调用authenticate(token)进行身份认证
然后委派组装成员ModularRealmAuthenticator 多realm认证器调用authenticate(token) 进行认证
然后调用利用AuthenticationStrategy认证策略调用realm.getAuthenticationInfo(token)
进行身份获取和matcher得到认证信息，最后得到认证结果

扩展口子为实现自己的realm,主要需要实现getAuthenticationInfo和Matcher和Support方法

2. 注解授权流程图
首先需要在对应的授权资源上添加RequiresPermissions和RequiresRoles注解
Shiro spring中存在PermissionAnnotationMethodInterceptor 和RoleAnnotationMethodInterceptor注解拦截器会对调用的资源方法方法进行拦截和权限验证处理
首先会调用assertAuthorized(methodInvocation) 方法授权
然后调用methodInvocation.getAnnotation获取注解，如果存在注解权限注解首先获取方法需要的权限rrAnnotation.value()
然后通过SecurityUtils获取Subject,然后调用调用checkRole（role）,checkRoles(roles) /checkPermission(permission),checkPermissions(permissions) 方法进行权限验证
然后会委托SecurityManager 进行校验，再委托ModularRealmAuthorizer 认证器进行校验
然后再遍历所有的Authorizer Realm，并获取认证信息AuthorizationInfo， 然后再比较权限是否满足
   
### 架构
``` 
shiro:
配置SecurityManager，配置不需要登录访问的接口
比如login,swagger,metrics,health，promethus ...基础接口
login接口主要是通过api中的用户名，密码进行Subject.login操作，登录成功jwtUtil写cookie
配置认证： 主要是通过过滤器实现，

Subject : 主体,具有login,logout 两个api
login(AuthenticateToken) 主要是通过代理到SecurityManager中处理，因此其中包含当前所有用户
SecurityManager 中有ModularReleamAuthenticator，该组件具有所有的Releam,
遍历所有的Releam 是否支持 token, 支持则进行认证，首先是通过Token 获取AuthenticationInfo
之后利用CredentialsMatcher 进行AuthenticateToken和AuthenticationInfo 校验；

主要实现的有两种Token：
一个UserPasswordToken用于用户登录认证，会利用密码进行匹配；
这里我们对密码实现了多种加密方式存储，包括，md5,bcrypt两种方式，默认是md5,在注册用户的时候可以配置用户密码等级
在校验的时候，根据解析密码得到用户密码hash方式，将输入的密码使用的对应的Hasher,将结果对比，登录成功写JWTToken
到Cookie中，用于session访问

另外一个是JWTToken用于用户session访问，每次访问时携带cookie，JWTFilter会创建Token进行身份认证，使用的Subject.login(token)
而后shiro会进行代理处理，调用JWTReleam 组件的匹配，主要是对jwt进行解析和有效期校验，如果有效则可以进行访问



``` 