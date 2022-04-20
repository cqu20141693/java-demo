## flink 

### flink 常见算子

#### map
1. 将元素进行映射转换

#### flatmap
1. 将一个元素打散为多个元素进行处理
```
 DataStream<String> textStream;

```
#### filter
1. filter是进行筛选: 不满足条件的剔除

#### keyBy
1. 逻辑上将Stream根据指定的Key进行分区，是根据key的散列值进行分区的。
2. 分区后可以进行window和aggregate 函数分析
3. 分区结果和KeyBy下游算子的并行度强相关。如下游算子只有一个并行度,不管怎么分，都会分到一起。

#### setParallelism
1. 设置并行度

#### name
1. 设置数据集的名称
```
设置当前数据流的名称。
绑定到数据源上的 name 属性是为了调试方便，
如果发生一些异常，我们能够通过它快速定位问题发生在哪里。

stream.name("stream1")
stream.print()
```

#### uid
1. 执行操作员的用户id
``` 
用户指定的uid，该uid的主要目的是用于在job重启时可以再次分配跟之前相同的uid，
应该是用于持久保存状态的目的。指定的ID用于在整个作业中分配相同的操作员ID提交
此ID必须是唯一的 否则，作业提交将失败。

stream.uid("source-1")
```

#### split 与 select
1. split 算子：将 DataStream 拆分成多个 DataStream
2. select算子：在SplitStream 中获取一个 DataStream
``` 
该算子已废弃，官方推荐侧切流
```
#### Side Output
1. 
``` 
```
#### reduce
1. 将keyedStream 转化为DataSet,将stream转化为一个计算结果；将结果集进行计算处理
``` 
```
#### fold
1. 给定初始值的计算处理
``` 
```
#### join
1. 将流按照某个key进行inner join
``` 
```
#### coGroup
1. 将流按照某个key进行join,不匹配的也保留
``` 
```
#### window
1. 创建时间处理窗口
``` 
```
#### apply
1. window 处理函数，进行数据处理收集
``` 
```
#### sum
1. 
``` 
```
#### min,minBy
1. 
``` 
```
#### avg
1. 
``` 
```
#### max,maxBy
1. 
``` 
```
#### union
1. 合并同类型的数据流、集
``` 
```
#### uid
1. 
``` 
```

