### dubbo spi

dubbo实现了一套功能更强的 SPI 机制,支持了AOP与依赖注入，并且利用缓存提高加载实现类的性能，同时支持实现类的灵活获取 通过ExtensionLoader 类实现按照类型同LoadStrategy进行配置文件进行扫描和读取

并实现了自适应extension 调用，setter 实例（IOC）和wrap 代理(AOP)

#### annotation 使用

1. @SPI("keyName")

``` 
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPI {

    /**
     * default extension name
     * 设置默认拓展类
     */
    String value() default "";

}
该注解只作用在接口上，value用来设置默认拓展类

@SPI("dubbo") 说的是，通过 SPI 机制来提供实现类，实现类是通过 keyName 作为默认key
去配置文件里找到的，配置文件名称与接口全限定名一样的

```

2. @Adaptive and URL : 动态自适应

``` 
接口方法可以使用Adaptive 注解，在运行时会生成接口代理类，

URL类：该类是 Dubbo 内置的类，代表了 Dubbo 整个执行过程中的上下文信息，包括各类配置信息，参数等

https://www.jianshu.com/p/f9e5ed9f7737
```

3. Wrapper AOP 思想实现代理

```  
如果定义了wrapper extension 则所有的instance 调用都会通过wrapper进行代理调用

1.持有扩展点接口对象属性，并通过构造器方式初始化该属性
2.这个类也要实现扩展点接口类，并在实现方法中进行增强操作
3. 可以多级wrap: 通过实现 添加Activate 注解
场景：
统计接口的耗时，日志
```   

4. ExtensionLoader

```  
代理创建 EXTENSION_LOADERS 缓存，利用

public class ExtensionLoader{

    private static final Pattern NAME_SEPARATOR = Pattern.compile("\\s*[,]+\\s*");
    // SPI 接口类 --> ExtensionLoader 缓存  ： 全局静态缓存，获取每个接口的Extention前置缓存
    private static final ConcurrentMap<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>(64);
    // Class -> instance 缓存： 全局静态缓存
    private static final ConcurrentMap<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<>(64);

    private final Class<?> type;

    // 非ExtenionFactory 为AdaptiveExtensionFactory  ： ExtendLoader 的私有属性
    private final ExtensionFactory objectFactory;

    private final ConcurrentMap<Class<?>, String> cachedNames = new ConcurrentHashMap<>();
    //Holder 缓存实例名称（文件中key值） --> 接口扩展实现Class缓存（文件中的value）  ： ExtendLoader 的私有属性
    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();

    private final Map<String, Object> cachedActivates = Collections.synchronizedMap(new LinkedHashMap<>());
    private final Map<String, Set<String>> cachedActivateGroups = Collections.synchronizedMap(new LinkedHashMap<>());
    private final Map<String, String[]> cachedActivateValues = Collections.synchronizedMap(new LinkedHashMap<>());
    
    //ConcurrentMap 缓存实例名称（文件中的key值）--> instance（文件中value Class实例化对象）  ：  ExtendLoader 的私有属性
    private final ConcurrentMap<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();
    private final Holder<Object> cachedAdaptiveInstance = new Holder<>();
    private volatile Class<?> cachedAdaptiveClass = null;
    private String cachedDefaultName;
    private volatile Throwable createAdaptiveInstanceError;

    private Set<Class<?>> cachedWrapperClasses;

    private Map<String, IllegalStateException> exceptions = new ConcurrentHashMap<>();

    private static volatile LoadingStrategy[] strategies = loadLoadingStrategies();
    
  ....
}
```

5. ExtensionFactory : dobbo IOC 思想

```  
/META-INF/dubbo/internel/org.apache.dubbo.common.extension.ExtensionFactory文件
SpiExtensionFactory
spring=org.apache.dubbo.config.spring.extension.SpringExtensionFactory
adaptive=org.apache.dubbo.common.extension.factory.AdaptiveExtensionFactory
spi=org.apache.dubbo.common.extension.factory.SpiExtensionFactory

```

6. LoadingStrategy ： 加载策略 多个定义和加载方式

``` 
/META-INF/dubbo/internel/org.apache.dubbo.common.extension.LoadingStrategy 文件

内置： 
DubboInternalLoadingStrategy  ： META-INF/dubbo/internal/
DubboLoadingStrategy ： META-INF/dubbo/
ServicesLoadingStrategy ：  META-INF/services/

```

7. Lifecycle : dubbo instance 生命周期：

```
public interface Lifecycle {

    void initialize() throws IllegalStateException;

    void start() throws IllegalStateException;

    void destroy() throws IllegalStateException;
}


```

### 参考

1. [dubbo SPI 原理](https://juejin.cn/post/6844904080251289607)
2. [dubbo Adaptive](https://www.jianshu.com/p/f9e5ed9f7737)
3. [dubbo wrapper](https://www.jianshu.com/p/676cb76afb3d)