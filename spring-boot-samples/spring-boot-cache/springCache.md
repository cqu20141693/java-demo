### spring cache

#### caffeine

1. maven

``` 
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-cache</artifactId>
    </dependency>
    <dependency>
      <groupId>com.github.ben-manes.caffeine</groupId>
      <artifactId>caffeine</artifactId>
    </dependency>
```

2. config CacheManager

``` 
    @Bean("selfManager")
    public CacheManager selfManager(ObjectProvider<CacheLoader<Object, Object>> cacheLoader) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(caffeineConfig());
        cacheLoader.ifAvailable(caffeineCacheManager::setCacheLoader);
        caffeineCacheManager.setCacheNames(names);
        caffeineCacheManager.setAllowNullValues(true);
        return caffeineCacheManager;
    }

或者利用spring boot 自动配置：CaffeineCacheConfiguration
spring:
  cache:
    cache-names: cacheOne
    caffeine:
      spec: maximumSize=500,expireAfterAccess=600s

```

3. annotation

``` 
@EnableCaching annotation : 开启缓存

@Cacheable: Triggers cache population.
@CacheEvict: Triggers cache eviction.
@CachePut: Updates the cache without interfering with the method execution.
@Caching: Regroups multiple cache operations to be applied on a method.
@CacheConfig: Shares some common cache-related settings at class-level.

重要的模型类
CaffeineCacheConfiguration
CacheProperties
CacheManagerCustomizers
Caffeine
CaffeineSpec
CaffeineSpec
s
CacheManager
```

#### custom caffeine cache

1. custom cache component
2. custom cache key
3. custom expire strategy