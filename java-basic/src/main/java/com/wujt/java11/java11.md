### java 11

Java 11是 Java 8 以后支持的首个长期版本。

#### 基于嵌套的访问控制

#### 新增String API

#### 标准 HTTP Client

#### 升级 Epsilon：低开销垃圾回收器
1. Epsilon 垃圾回收器和其他 OpenJDK 的垃圾回收器一样，可以通过参数 -XX:+UseEpsilonGC 开启
2. 只进行对内存分配，不进行垃圾回收
#### 简化启动单个源代码文件的方法

#### 用于 Lambda 参数的局部变量语法

#### 低开销的 Heap Profiling

#### 支持 TLS 1.3 协议

#### ZGC：可伸缩低延迟垃圾收集器
1. ZGC 是一个可伸缩的、低延迟的垃圾收集器
``` 
GC 停顿时间不超过 10ms,停顿时间不会随着堆的大小，或者活跃对象的大小而增加；
即能处理几百 MB 的小堆，也能处理几个 TB 的大堆
应用吞吐能力不会下降超过 15%（与 G1 回收算法相比）

https://tech.meituan.com/2020/08/06/new-zgc-practice-in-meituan.html
```