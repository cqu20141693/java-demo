### spring security

``` 
architecture
```

#### SecurityFilterAutoConfiguration

1. SecurityAutoConfiguration
2. EnableWebSecurity

#### SecurityContext

``` 
public interface SecurityContext extends Serializable {
	Authentication getAuthentication();
	void setAuthentication(Authentication authentication);
}
```

##### SecurityContextHolder

``` 
private static SecurityContextHolderStrategy strategy;
```

##### SecurityContextHolderStrategy

1. ThreadLocalSecurityContextHolderStrategy

``` 
private static final ThreadLocal<SecurityContext> contextHolder = new ThreadLocal<>();

	@Override
	public SecurityContext getContext() {
		SecurityContext ctx = contextHolder.get();
		if (ctx == null) {
			ctx = createEmptyContext();
			contextHolder.set(ctx);
		}
		return ctx;
	}
```

2. GlobalSecurityContextHolderStrategy

``` 
private static SecurityContext contextHolder;
	@Override
	public SecurityContext getContext() {
		if (contextHolder == null) {
			contextHolder = new SecurityContextImpl();
		}
		return contextHolder;
	}
```

3. InheritableThreadLocalSecurityContextHolderStrategy

``` 
private static final ThreadLocal<SecurityContext> contextHolder = new InheritableThreadLocal<>();
	@Override
	public SecurityContext getContext() {
		SecurityContext ctx = contextHolder.get();
		if (ctx == null) {
			ctx = createEmptyContext();
			contextHolder.set(ctx);
		}
		return ctx;
	}
```

#### FilterChainProxy

##### WebAsyncManagerIntegrationFilter

``` 
AsyncManager 设置
```

1. SecurityContextCallableProcessingInterceptor

``` 
主要职责： SecurityContext 设置和清理
```

##### SecurityContextPersistenceFilter

``` 
SecurityContext  Persistence : session with SPRING_SECURITY_CONTEXT

对session 进行持久化，主要时内存中Map 存储SessionId-> SecurityContext
```

##### HeaderWriterFilter

```
写response header:默认先过滤器链通过再写

```

1. ReferrerPolicyHeaderWriter
2. XContentTypeOptionsHeaderWriter
3. XXssProtectionHeaderWriter
4.

##### LogoutFilter

```
匹配登出path:
handler.logout(): 做登出操作
logoutSuccessHandler.onLogoutSuccess ： 登出成功响应操作

```

##### UsernamePasswordAuthenticationFilter

``` 
匹配登录path:
attemptAuthentication : 获取认证对象（username,password）并getAuthenticationManager().authenticate(authRequest)
sessionStrategy.onAuthentication: session authenticate(CsrfAuthenticationStrategy)
SecurityContextHolder.getContext().setAuthentication(authResult);
rememberMeServices.loginSuccess(request, response, authResult)
successHandler.onAuthenticationSuccess(request, response, authResult) 认证成功处理
```

##### RequestCacheAwareFilter

``` 
请求缓存： cookie,session,null
```

##### SecurityContextHolderAwareRequestFilter

``` 

```

##### AnonymousAuthenticationFilter

``` 
匹配getAuthentication() == null：
创建一个匿名用户：
SecurityContextHolder.getContext().setAuthentication(this.createAuthentication((HttpServletRequest)req))
```

##### SessionManagementFilter

``` 
做session认证
sessionAuthenticationStrategy.onAuthentication(authentication, request, response);
```

##### ExceptionTranslationFilter

``` 
异常处理过滤器： 对过滤器链进行 try catch
```

##### FilterSecurityInterceptor

``` 
主要职责： 对url资源进行授权验证

 "permitAll";
    private static final String denyAll = "denyAll";
    private static final String anonymous = "anonymous";
    private static final String authenticated = "authenticated";
    private static final String fullyAuthenticated = "fullyAuthenticated";
    private static final String rememberMe = "rememberMe";

```

##### 其他扩展Filter

1. DigestAuthenticationFilter

```
   String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Digest "){
        do authentication
        }
```
2. BasicAuthenticationFilter
``` 
   String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Basic"){
        do authentication
        }
```

#### ApplicationFilterChain

``` 
servlet fliter chain
```

##### OrderedCharacterEncodingFilter

1. order: -2147483648

##### OrderedFormContentFilter

1. order:  -9900

##### OrderedRequestContextFilter

1. order: -105

##### DelegatingFilterProxyRegistrationBean,DelegatingFilterProxy

``` 
spirng DelegatingFilterProxy : spring security filter chain relize
we can use it for realizing self filter chain

getFilter() return DelegatingFilterProxy:  init ApplictionContext

DelegatingFilterProxy use ApplicationContext  load springSecurityFilterChain bean(FilterChainProxy)

invoke FilterChainProxy Realize the execution of the security filter chain
```

1. function

``` 
proxy FilterRegistrationBean realize the registration of Filter
```

2. FilterRegistrationBean

``` 
创建 spring security filter chain: FilterChainProxy
执行过滤器链
```

##### WsFilter

1. order

#### AntPathMatcher

#### web Security

1. cors

``` 
FilterChainProxy
```