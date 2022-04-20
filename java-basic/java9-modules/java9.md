### java 9

#### 模块化

1. https://www.liaoxuefeng.com/wiki/1252599548343744/1281795926523938
2. https://zhuanlan.zhihu.com/p/24800180

#### jshell

1. jshell 是 Java 9 新增的开发工具，可以用于执行 Java 代码并立即获得执行结果。
2. https://zhuanlan.zhihu.com/p/28828422

#### java doc 生成的文档兼容 HTML5 标准

#### 新增集合工厂方法

1. List，Set 和 Map 接口中，新的静态工厂方法可以创建这些集合的不可变实例

#### 私有接口方法

1. java 8 支持了，常量定义，默认方法和静态方法
2. java 9 支持私有方法 ，私有静态方法

#### 改进的进程 API

1. 可以获取进程的启动时间，执行时长，pid,父子进程信息

#### 改进的 Stream API

1. java 9 为 Stream 新增了几个方法：dropWhile、takeWhile、ofNullable，为 iterate 方法新增了一个重载方法。

#### 改进的 Optional 类

1. java 9 中, 添加了三个方法来改进它的功能： stream(),ifPresentOrElse(),or()

#### 改进的 try-with-resources

1. 无需在 try-with-resources 语句中声明一个新变量,try-with-resources将确保每个资源在语句结束时关闭

#### 改进的 CompletableFuture API

1. ava 9 对 CompletableFuture 做了改进：支持 delays 和 timeouts,提升了对子类化的支持,新的工厂方法