### spi
#### jdk spi
1. 定义接口
2. 通过ServiceLoader类加载实现
``` 
原理： 
先找当前线程绑定的 ClassLoader，如果没有就用 SystemClassLoader，
然后清除一下缓存，再创建一个 LazyIterator;当调用了 hasNext() 来做实例循环，
通过 next() 得到一个实例。

当调用next()方法时才会进行真正的文件查找和类加载，会加载所有的实现类

```