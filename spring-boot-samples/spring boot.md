### spring boot

#### core

1. Spring Boot 的核心注解是哪个？它主要由哪几个注解组成的？ 启动类上面的注解是@SpringBootApplication，它也是 Spring Boot 的核心注解，主要组合包含了以下 3 个注解：

@SpringBootConfiguration：组合了 @Configuration 注解，实现配置文件的功能。 @ComponentScan：Spring组件扫描。
@EnableAutoConfiguration：打开自动配置的功能，也可以关闭某个自动配置的选项，如关闭数据源自动配置功能： @SpringBootApplication(exclude = {
DataSourceAutoConfiguration.class })。 利用@Import({AutoConfigurationImportSelector.class})
会加载类路径及所有jar包下META-INF/spring.factories配置中映射的自动配置的类：
spring默认实现了常用的数据库，mq的自动配置，我们自己也可以实现；并利用@ConditionalOnClass就是自动配置的核心，首先它得是一个配置文件，其次根据类路径下是否有这个类去自动配置。

2. Spring Boot 自动配置原理是什么？ SpringBootApplication 注解上自带了EnableAutoConfiguration
   注解，而这个注解具有一个Import注解，import注解提供了一个Selector类，
   这个类会自己加载所有的jar中的mata-info文件夹在的spring.factories文件中配置的自动配置类，一般这些配置类都使用了Conditional 条件注解，只有使用对应的starter才会自动装载对应的bean,
   从而提供服务 注解 @EnableAutoConfiguration, @Configuration, @ConditionalOnClass 就是自动配置的核心，首先它得是一个配置文件，其次根据类路径下是否有这个类去自动配置。

3. spring boot 是什么？ spring boot是spring旗下的一个微服务开发子项目，其提供了自动化配置，可独立运行的特性，方面开发者进行快速开发和部署应用的能力

#### annotation

1. @ConditionalOnProperty

``` 
1、不配置@ConditionalOnProperty，直接生效。
2、@ConditionalOnProperty(value = "test.conditional")
只有value属性，没有havingValue属性。如果application.yml配置了test.conditional则生效，否则不生效
3. @ConditionalOnProperty(prefix = "test", name = "conditional")
prefix + name相当于value属性(两者不可同时使用)。如果application.yml配置了test.conditional则生效，否则不生效
4. @ConditionalOnProperty(prefix = "test", name = { "conditional", "other" })
name属性为一个数组，当要匹配多个值时，如果application.yml的配置与name属性中的值一一匹配则生效，否则不生效
5. @ConditionalOnProperty(prefix = "test", name = "conditional", havingValue = "abc")
加上havingValue属性,当havingValue的值与application.yml文件中test.conditional的值一致时则生效，否则不生效
6.@ConditionalOnProperty(prefix = "test", name = "conditional",  havingValue = "abc"，matchIfMissing = true)
 加上matchIfMissing=true时，如果不存在配置或者配置值等于havingValue时生效，否则不生效
```

### spring boot version

1. 可通过新建类似boot-2.4.5-dependencies 模块进行parent引用测试