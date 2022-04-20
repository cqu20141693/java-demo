### spring batch

#### 核心概念

1. job

表示是Spring batch一次批处理的过程。其定义了批处理执行的具体逻辑，封装整个批处理过程的实体。

2. Step

批处理任务中的某一步骤，每个处理job由一个或者多个步骤组成。

3. ExecutionContext

批处理执行上下文，其能够将所需的参数在处理任务的过程中进行传递。

4. JobRepository

提供对处理任务的持久化操作。

5. JobLauncher

其主要是为处理任务job提供了一个启动器，同时在启动job的时候我们可以传递自定义的参数。

6. Item Reader

其可以给Step提供数据的输入，当数据读取结束后， 其会返回null来告知内容已经结束。

7. Item Writer

作为数据的输出，spring batch提供了一个chunk参数，任务一次会写入一个chunk的数据，而chunk的数量取决于任务启动时候的配置。

8. Item Processor

此内容复制在数据读入Step到写出之间对数据处理的逻辑。

9. step的流程

Reader -> Processor -> Writer

#### 配置使用

The first method defines the job, and the second one defines a single step. Jobs are built from steps, where each step
can involve a reader, a processor, and a writer.

1. maven 配置
2. java 配置

``` 
@EnableBatchProcessing是打开Batch。如果要实现多Job的情况，需要把EnableBatchProcessing
注解的modular设置为true，让每个Job使用自己的ApplicationConext。
```