# 项目简介

Cache 用于实现一个可拓展的本地缓存。

有人的地方，就有江湖。

有高性能的地方，就有 cache。

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.houbb/cache/badge.svg)](http://mvnrepository.com/artifact/com.github.houbb/cache)
[![Build Status](https://www.travis-ci.org/houbb/cache.svg?branch=master)](https://www.travis-ci.org/houbb/cache?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/houbb/cache/badge.svg?branch=master)](https://coveralls.io/github/houbb/cache?branch=master)
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

- 支持 expire 过期特性

- 支持自定义 map 实现策略

- 支持自定义 evict 驱除策略

- 日志整合框架，自适应常见日志

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
    <version>0.0.5</version>
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

# 后期 Road-MAP

- [ ] CRUD 监听类

- [ ] 添加 persist 持久化

- [ ] 添加 load 加载

- [ ] 耗时统计，慢日志统计

- [ ] spring 整合

提供 `@Cacheable` 系列注解