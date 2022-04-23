# 项目简介

[Cache](https://github.com/houbb/cache) 用于实现一个可拓展的本地缓存。

有人的地方，就有江湖。

有高性能的地方，就有 cache。

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.houbb/cache/badge.svg)](http://mvnrepository.com/artifact/com.github.houbb/cache)
[![Build Status](https://www.travis-ci.org/houbb/cache.svg?branch=master)](https://www.travis-ci.org/houbb/cache?branch=master)
[![](https://img.shields.io/badge/license-Apache2-FF0080.svg)](https://github.com/houbb/cache/blob/master/LICENSE.txt)
[![Open Source Love](https://badges.frapsoft.com/os/v2/open-source.svg?v=103)](https://github.com/houbb/cache)

## 创作目的

- 为日常开发提供一套简单易用的缓存框架

- 便于后期多级缓存开发

- 学以致用，开发一个类似于 redis 的渐进式缓存框架

## 特性

- MVP 开发策略

- fluent 流式编程体验，纵享丝滑

- 支持 cache 固定大小

- 支持自定义 map 实现策略

- 支持 expire 过期特性

- 支持自定义 evict 驱除策略

内置 FIFO 和 LRU 驱除策略

- 支持自定义删除监听器

- 日志整合框架，自适应常见日志

- 支持 load 初始化和 persist 持久化

RDB 和 AOF 两种模式

# 变更日志

> [变更日志](https://github.com/houbb/cache/blob/master/doc/CHANGELOG.md)

# 快速开始

## 准备

JDK1.7 及其以上版本

Maven 3.X 及其以上版本

## maven 项目依赖

```xml
<dependency>
    <groupId>com.github.houbb</groupId>
    <artifactId>cache-core</artifactId>
    <version>0.0.15</version>
</dependency>
```

## 入门测试

```java
ICache<String, String> cache = CacheBs.<String,String>newInstance()
                .size(2)
                .build();

cache.put("1", "1");
cache.put("2", "2");
cache.put("3", "3");
cache.put("4", "4");

Assert.assertEquals(2, cache.size());
```

默认为先进先出的策略，此时输出 keys，内容如下：

```
[3, 4]
```

## 引导类配置属性

`CacheBs` 作为缓存的引导类，支持 fluent 写法，编程更加优雅便捷。

上述配置等价于：

```java
ICache<String, String> cache = CacheBs.<String,String>newInstance()
                .map(Maps.<String,String>hashMap())
                .evict(CacheEvicts.<String, String>fifo())
                .size(2)
                .build();
```

## 淘汰策略

目前内置了几种淘汰策略，可以直接通过 `CacheEvicts` 工具类创建。

| 策略 | 说明 |
|:---|:---|
| none | 没有任何淘汰策略 |
| fifo | 先进先出（默认策略） |
| lru | 最基本的朴素 LRU 策略，性能一般 |
| lruDoubleListMap | 基于双向链表+MAP 实现的朴素 LRU，性能优于 lru |
| lruLinkedHashMap | 基于 LinkedHashMap 实现的朴素 LRU，与 lruDoubleListMap 差不多 |
| lru2Q | 基于 LRU 2Q 的改进版 LRU 实现，命中率优于朴素LRU |
| lru2 | 基于 LRU-2 的改进版 LRU 实现，命中率优于 lru2Q |

## 过期支持

```java
ICache<String, String> cache = CacheBs.<String,String>newInstance()
        .size(3)
        .build();

cache.put("1", "1");
cache.put("2", "2");

cache.expire("1", 10);
Assert.assertEquals(2, cache.size());

TimeUnit.MILLISECONDS.sleep(50);
Assert.assertEquals(1, cache.size());
System.out.println(cache.keySet());
```

`cache.expire("1", 10);` 指定对应的 key 在 10ms 后过期。

# 删除监听器

## 说明

淘汰和过期，这些都是缓存的内部行为。

如果用户也关心的话，可以自定义删除监听器。

## 自定义监听器

直接实现 `ICacheRemoveListener` 接口即可。

```java
public class MyRemoveListener<K,V> implements ICacheRemoveListener<K,V> {

    @Override
    public void listen(ICacheRemoveListenerContext<K, V> context) {
        System.out.println("【删除提示】可恶，我竟然被删除了！" + context.key());
    }

}
```

## 使用

```java
ICache<String, String> cache = CacheBs.<String,String>newInstance()
        .size(1)
        .addRemoveListener(new MyRemoveListener<String, String>())
        .build();

cache.put("1", "1");
cache.put("2", "2");
```

- 测试日志

```
【删除提示】可恶，我竟然被删除了！2
```

# 添加慢操作监听器

## 说明

redis 中会存储慢操作的相关日志信息，主要是由两个参数构成：

（1）slowlog-log-slower-than 预设阈值,它的单位是毫秒(1秒=1000000微秒)默认值是10000

（2）slowlog-max-len 最多存储多少条的慢日志记录

不过 redis 是直接存储到内存中，而且有长度限制。

根据实际工作体验，如果我们可以添加慢日志的监听，然后有对应的存储或者报警，这样更加方便问题的分析和快速反馈。

所以我们引入类似于删除的监听器。

## 自定义监听器

实现接口 `ICacheSlowListener`

这里每一个监听器都可以指定自己的慢日志阈值，便于分级处理。

```java
public class MySlowListener implements ICacheSlowListener {

    @Override
    public void listen(ICacheSlowListenerContext context) {
        System.out.println("【慢日志】name: " + context.methodName());
    }

    @Override
    public long slowerThanMills() {
        return 0;
    }

}
```

## 使用

```java
ICache<String, String> cache = CacheBs.<String,String>newInstance()
        .addSlowListener(new MySlowListener())
        .build();

cache.put("1", "2");
cache.get("1");
```

- 测试效果

```
[DEBUG] [2020-09-30 17:40:11.547] [main] [c.g.h.c.c.s.i.c.CacheInterceptorCost.before] - Cost start, method: put
[DEBUG] [2020-09-30 17:40:11.551] [main] [c.g.h.c.c.s.i.c.CacheInterceptorCost.after] - Cost end, method: put, cost: 10ms
【慢日志】name: put
[DEBUG] [2020-09-30 17:40:11.554] [main] [c.g.h.c.c.s.i.c.CacheInterceptorCost.before] - Cost start, method: get
[DEBUG] [2020-09-30 17:40:11.554] [main] [c.g.h.c.c.s.i.c.CacheInterceptorCost.after] - Cost end, method: get, cost: 1ms
【慢日志】name: get
```

实际工作中，我们可以针对慢日志数据存储，便于后期分析。

也可以直接接入报警系统，及时反馈问题。

# 添加 load 加载器

## 说明

有时候我们需要在 cache 初始化的时候，添加对应的数据初始化。

后期可以从文件等地方加载数据。

## 实现

实现 `ICacheLoad` 接口即可。

```java
public class MyCacheLoad implements ICacheLoad<String,String> {

    @Override
    public void load(ICache<String, String> cache) {
        cache.put("1", "1");
        cache.put("2", "2");
    }

}
```

我们在缓存初始化的时候，放入 2 个元素。

## 测试效果

```java
ICache<String, String> cache = CacheBs.<String,String>newInstance()
        .load(new MyCacheLoad())
        .build();

Assert.assertEquals(2, cache.size());
```

# 添加 persist 持久化类

## 说明

如果我们只是把文件放在内存中，应用重启信息就丢失了。

有时候我们希望这些 key/value 信息可以持久化，存储到文件或者 database 中。

## 持久化

`CachePersists.<String, String>dbJson("1.rdb")` 指定将数据文件持久化到文件中。

定期执行，暂时全量持久化的间隔为 10min，后期考虑支持更多配置。

```java
public void persistTest() throws InterruptedException {
    ICache<String, String> cache = CacheBs.<String,String>newInstance()
            .load(new MyCacheLoad())
            .persist(CachePersists.<String, String>dbJson("1.rdb"))
            .build();

    Assert.assertEquals(2, cache.size());
    TimeUnit.SECONDS.sleep(5);
}
```

- 1.rdb

文件内容如下：

```
{"key":"2","value":"2"}
{"key":"1","value":"1"}
```

## 加载器

存储之后，可以使用对应的加载器读取文件内容：

```java
ICache<String, String> cache = CacheBs.<String,String>newInstance()
        .load(CacheLoads.<String, String>dbJson("1.rdb"))
        .build();

Assert.assertEquals(2, cache.size());
```

# 后期 Road-MAP

## 淘汰策略

- [ ] CLOCK 算法

- [ ] SC 二次机会

- [ ] 老化算法

- [ ] 弱引用

## 过期特性

- [ ] 过期策略添加随机返回

- [ ] expireAfterWrite()

- [ ] expireAfterAccess()

## 持久化

- [ ] AOF 混合 RDB

## 统计

- [ ] 命中率

- [ ] keys 数量

- [ ] evict 数量

- [ ] expire 数量

- [ ] 耗时统计

## 并发

- [ ] 并发安全保障

## 其他

- [ ] 异步 callable 操作

- [ ] spring 整合

提供 `@Cacheable` 系列注解

- [ ] 文件压缩

- [ ] 独立服务端

提供类似于 redis-server + redis-client 的拆分，便于独立于应用作为服务存在。

# 拓展阅读

[java从零手写实现redis（一）如何实现固定大小的缓存？](https://mp.weixin.qq.com/s/6J2K2k4Db_20eGU6xGYVTw)

[java从零手写实现redis（三）redis expire 过期原理](https://mp.weixin.qq.com/s/BWfBc98oLqhAPLN2Hgkwow)

[java从零手写实现redis（三）内存数据如何重启不丢失？](https://mp.weixin.qq.com/s/G41SRZQm1_0uQXBAGHAYbw)

[java从零手写实现redis（四）添加监听器](https://mp.weixin.qq.com/s/6pIG3l_wkXBwSuJvj_KwMA)

[java从零手写实现redis（五）过期策略的另一种实现思路](https://mp.weixin.qq.com/s/Atrd36UGds9_w_NFQDoEQg)

[java从零手写实现redis（六）AOF 持久化原理详解及实现](https://mp.weixin.qq.com/s/rFuSjNF43Ybxy-qBCtgasQ)

[java从零开始手写redis（七）LRU 缓存淘汰策略详解](https://mp.weixin.qq.com/s/X-OIqu_rgLskvbF2rZMP6Q)

[java从零开始手写redis（八）朴素 LRU 淘汰算法性能优化](https://mp.weixin.qq.com/s/H8gOujnlTinctjVQqW0ITA)

[java 从零开始手写 redis（九）LRU 缓存淘汰算法如何避免缓存污染](https://mp.weixin.qq.com/s/jzM_wDw37QXTeYMFYtRJaw)

[java 从零开始手写 redis（十）缓存淘汰算法 LFU 最少使用频次](https://mp.weixin.qq.com/s/mUyCTCVObwY8XdLcO1pOWg)

[java 从零开始手写 redis（11）clock时钟淘汰算法详解及实现](https://mp.weixin.qq.com/s/h9oub0TT94ObaiKZ7s5VsA)

[java 从零开始手写 redis（12）redis expire 过期如何实现随机获取keys？](https://mp.weixin.qq.com/s/YEBPtrOWIyBl9dsHa59JYg)

[java从零开始手写 redis（13）HashMap 源码原理详解](https://mp.weixin.qq.com/s/SURVmTf6K_ou85fShFzrNA)

[java 从零开始手写 redis（14）redis渐进式rehash详解](https://mp.weixin.qq.com/s/gPD-4wCwirdyO5QyHrXOIA)

[java 从零开始手写 redis（15）实现自己的 HashMap](https://mp.weixin.qq.com/s/e5fskVfeDMTuJhjEAd1gQw)

[java 从零开始手写 redis（16）实现渐进式 rehash map](https://mp.weixin.qq.com/s/Lwp2js4lrHAbuQ5Fexer6w)

【实战汇总】

[缓存实战（1）缓存雪崩、缓存击穿和缓存穿透入门简介及解决方案](https://mp.weixin.qq.com/s/yYE-zqJOyiLlEYXRj5by9g)

[缓存实战（2）布隆过滤器是啥？guava 的 BloomFilter 使用](https://mp.weixin.qq.com/s/dY-0jE23jggU3wqjdHGyZQ)

[缓存实战（3）让你彻底搞懂布隆过滤器！实现一个自己的BloomFilter](https://mp.weixin.qq.com/s/UsIjHfiy96aZgpzybuBYgg)

[缓存实战（4）bloom filter 使用最佳实践，让你少踩坑](https://mp.weixin.qq.com/s/obqh0FMzahRFa5sNe5eq3g)

[java 从零实现属于你的 redis 分布式锁](https://mp.weixin.qq.com/s/MzybPDRGwaWXX8viE8adAA)

[3天时间，我是如何解决redis bigkey删除问题的？](https://mp.weixin.qq.com/s/06tjn76uebvgfzYaahdY0g)
