### spring async

#### TaskExecutor

1. SyncTaskExecutor

```
   This implementation does not run invocations asynchronously. Instead, each invocation takes place in the calling thread. It is primarily used in situations where multi-threading is not necessary, such as in simple test cases.
```

2. SimpleAsyncTaskExecutor

```   
This implementation does not reuse any threads. Rather, it starts up a new thread for each
invocation. However, it does support a concurrency limit that blocks any invocations that are over the limit until a
slot has been freed up. If you are looking for true pooling, see ThreadPoolTaskExecutor, later in this list.
```

3. ConcurrentTaskExecutor

```   
This implementation is an adapter for a java.util.concurrent.Executor instance. There is an
alternative (ThreadPoolTaskExecutor) that exposes the Executor configuration parameters as bean properties. There is
rarely a need to use ConcurrentTaskExecutor directly. However, if the ThreadPoolTaskExecutor is not flexible enough for
your needs, ConcurrentTaskExecutor is an alternative.
```

4. ThreadPoolTaskExecutor : This implementation is most commonly used.

```   
It exposes bean properties for configuring a
java.util.concurrent.ThreadPoolExecutor and wraps it in a TaskExecutor. If you need to adapt to a different kind of
java.util.concurrent.Executor, we recommend that you use a ConcurrentTaskExecutor instead.
```

5. WorkManagerTaskExecutor:

```   
This implementation uses a CommonJ WorkManager as its backing service provider and is the
central convenience class for setting up CommonJ-based thread pool integration on WebLogic or WebSphere within a Spring
application context.
```

6. DefaultManagedTaskExecutor

```  
This implementation uses a JNDI-obtained ManagedExecutorService in a JSR-236 compatible
runtime environment (such as a Java EE 7+ application server), replacing a CommonJ WorkManager for that purpose.
``` 

#### TaskScheduler

1. interface TaskScheduler

```  
 public interface TaskScheduler {

   ScheduledFuture schedule(Runnable task, Trigger trigger);

   ScheduledFuture schedule(Runnable task, Instant startTime);

   ScheduledFuture schedule(Runnable task, Date startTime);

   ScheduledFuture scheduleAtFixedRate(Runnable task, Instant startTime, Duration period);

   ScheduledFuture scheduleAtFixedRate(Runnable task, Date startTime, long period);

   ScheduledFuture scheduleAtFixedRate(Runnable task, Duration period);

   ScheduledFuture scheduleAtFixedRate(Runnable task, long period);

   ScheduledFuture scheduleWithFixedDelay(Runnable task, Instant startTime, Duration delay);

   ScheduledFuture scheduleWithFixedDelay(Runnable task, Date startTime, long delay);

   ScheduledFuture scheduleWithFixedDelay(Runnable task, Duration delay);

   ScheduledFuture scheduleWithFixedDelay(Runnable task, long delay);
   }
   The simplest method is the one named schedule that takes only a Runnable and a Date. 
   That causes the task to run once after the specified time. All of the other methods are
    capable of scheduling tasks to run repeatedly. The fixed-rate and fixed-delay methods are for simple,
     periodic execution, but the method that accepts a Trigger is much more flexible.
 ```

2. [ThreadPoolTaskScheduler](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#scheduling-task-scheduler)

#### Trigger

1. Trigger Interface

``` 
public interface Trigger {

    Date nextExecutionTime(TriggerContext triggerContext);
}
```

2. CronTrigger

``` 
CronTriggerFactoryBean 

```

3. PeriodicTrigger

``` 
scheduleAtFixedRate 和scheduleWithFixedDelay 方法的触发器实现

```

4. TriggerContext

``` 
public interface TriggerContext {

    Date lastScheduledExecutionTime();

    Date lastActualExecutionTime();

    Date lastCompletionTime();
}

The TriggerContext is the most important part. It encapsulates all of the relevant data 
and is open for extension in the future, if necessary. The TriggerContext is an interface
(a SimpleTriggerContext implementation is used by default). 

```

####   

#### corn expression

1. [corn expressions](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#scheduling-cron-expression)
2. rule

``` 
 ┌───────────── second (0-59)
 │ ┌───────────── minute (0 - 59)
 │ │ ┌───────────── hour (0 - 23)
 │ │ │ ┌───────────── day of the month (1 - 31)
 │ │ │ │ ┌───────────── month (1 - 12) (or JAN-DEC)
 │ │ │ │ │ ┌───────────── day of the week (0 - 7)
 │ │ │ │ │ │          (0 or 7 is Sunday, or MON-SUN)
 │ │ │ │ │ │
 * * * * * *
 
 
```

#### . Annotation Support

1. Enable Scheduling Annotations

``` 
To enable support for @Scheduled and @Async annotations, you can add
 @EnableScheduling and @EnableAsync to one of your @Configuration classes
```

2. config SchedulingConfigurer, AsyncConfigurer

``` 
public interface SchedulingConfigurer {
    void configureTasks(ScheduledTaskRegistrar var1);
}
public interface AsyncConfigurer {
    @Nullable
    default Executor getAsyncExecutor() {
        return null;
    }

    @Nullable
    default AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}
```

3. @Async 、 @Scheduled 注解使用

``` 


```

4. Executor 、 TaskScheduler bean 使用

```  


```

#### 參考

1. [定时任务的使用](https://blog.lqdev.cn/2018/08/19/springboot/chapter-twenty-two/#SchedulingConfigurer)