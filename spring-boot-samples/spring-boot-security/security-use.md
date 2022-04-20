### security use

#### config

1. maven

``` 
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

```

2. 配置

```  
// 配置login page
// 配置登录成功，失败handler
// 配置 authorize config
// 配置 logout 配置
// 配置 http security

public class SecurityConfig extends WebSecurityConfigurerAdapter {

}
```

#### [注解权限](https://juejin.cn/post/6844904000026837005#heading-3)

1. config

``` 
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true,jsr250Enabled = true)

 public @interface EnableGlobalMethodSecurity {
 
 	/**
 	 * 基于表达式进行方法访问控制
 	 * @PreAuthorize 注解会在方法执行前进行验证，而 @PostAuthorize 注解会在方法执行后进行验证
 	 *   @PreAuthorize 和 @PostAuthorize 侧重于方法调用的控制；而 @PreFilter 和 @PostFilter 侧重于数据的控制
 	 */
 	boolean prePostEnabled() default false;
 
 	/**
 	 * 基于 @Secured 注解
 	 */
 	boolean securedEnabled() default false;
 
 	/**
     * 基于 JSR-250 注解
     * @DenyAll 拒绝所有的访问
     * @PermitAll 同意所有的访问
     * @RolesAllowed 用法和 @Secured 一样。
 	 */
 	boolean jsr250Enabled() default false;

 	boolean proxyTargetClass() default false;

 	int order() default Ordered.LOWEST_PRECEDENCE;
 }
 


```

2. @PreAuthorize

``` 
在标记的方法调用之前，通过表达式来计算是否可以授权访问

@PreAuthorize("hasRole('ADMIN')") 必须拥有 ROLE_ADMIN 角色
@PreAuthorize("principal.username.startsWith('Felordcn')") 用户名开头为 Felordcn 的用户才能访问。
基于对入参的 SpEL表达式处理： @PreAuthorize("#id.equals(principal.username)") 入参 id 必须同当前的用户名相同。
```

2. @PostAuthorize

``` 
在标记的方法调用之后，通过表达式来计算是否可以授权访问

如果方法没有返回值实际上等于开放权限控制；如果有返回值实际的结果是用户操作成功但是得不到响应。
```

3. @PreFilter

``` 

```

4. @PostFilter

``` 

```

5. @Secured

```  
该注解功能要简单的多，默认情况下只能基于角色（默认需要带前缀 ROLE_）集合来进行访问控制决策。
该注解的机制是只要其声明的角色集合（value）中包含当前用户持有的任一角色就可以访问。也就是 用户的角色集合和
 @Secured 注解的角色集合要存在非空的交集。  不支持使用 SpEL 表达式进行决策。
```

6. @DenyAll 拒绝所有的访问
7. @PermitAll 同意所有的访问
8. @RolesAllowed 用法和 5. 中的 @Secured 一样