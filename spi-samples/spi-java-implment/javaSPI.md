### java SPI
使用简单，功能不强且一次性加载所有的扩展类
#### 原理
1. Java SPI约定
``` 
当服务的提供者，提供了服务接口的一种实现之后，在jar包的META-INF/services/目录里同时创建一个以服务接口命名的文件。
该文件里就是实现该服务接口的具体实现类。而当外部程序装配这个模块的时候，就能通过该jar包META-INF/services/里的配置
文件找到具体的实现类名，并装载实例化，完成模块的注入。 基于这样一个约定就能很好的找到服务接口的实现类，而不需要再在代码
里指定。JDK中提供了服务实现查找的一个工具类：java.util.ServiceLoader
```
2. ServiceLoader
```
public final class ServiceLoader<S> implements Iterable<S> {
    // 读取配置文件的前缀路径 META-INF/services/
    private static final String PREFIX = "META-INF/services/";
    // 需要被加载的服务接口或者服务类
    private final Class<S> service;
    // 类加载器
    private final ClassLoader loader;
    // 创建ServiceLoader时采用的访问控制上下文，默认情况下为 null
    private final AccessControlContext acc;
    // 缓存SPI的实现，key是完整类名
    private LinkedHashMap<String,S> providers = new LinkedHashMap<>();
    // 当前的迭代器，默认初始化为: new LazyIterator(service, loader)
    // 这里是懒加载的，只有使用的时候才去迭代,加载
    // LazyIterator 是 ServiceLoader 的内部类
    private LazyIterator lookupIterator;
}

// LazyIterator 是 ServiceLoader的内部类 ，代理进行文件查找，加载和类的反射创建
private class LazyIterator implements Iterator<S> {
    Class<S> service;
    ClassLoader loader;
    Enumeration<URL> configs = null;
    Iterator<String> pending = null;
    String nextName = null;

    private LazyIterator(Class<S> service, ClassLoader loader) {
        this.service = service;
        this.loader = loader;
    }
}

```
3. [spi原理](https://learnku.com/articles/45733)
