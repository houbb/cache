package com.github.houbb.cache.core.bs;

import com.github.houbb.cache.api.*;
import com.github.houbb.cache.core.core.CacheContext;
import com.github.houbb.cache.core.core.Caches;
import com.github.houbb.cache.core.support.evict.CacheEvicts;
import com.github.houbb.cache.core.support.expire.CacheExpireContext;
import com.github.houbb.cache.core.support.expire.CacheExpires;
import com.github.houbb.cache.core.support.interceptor.CacheInterceptors;
import com.github.houbb.cache.core.support.load.CacheLoadContext;
import com.github.houbb.cache.core.support.load.CacheLoads;
import com.github.houbb.cache.core.support.map.CacheMaps;
import com.github.houbb.cache.core.support.persist.CachePersistContext;
import com.github.houbb.cache.core.support.persist.CachePersists;
import com.github.houbb.heaven.util.common.ArgUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 缓存引导类
 * @author binbin.hou
 * @since 0.0.2
 */
public final class CacheBs<K,V> {

    private CacheBs(){}

    /**
     * 创建对象实例
     * @param <K> key
     * @param <V> value
     * @return this
     * @since 0.0.2
     */
    public static <K,V> CacheBs<K,V> newInstance() {
        return new CacheBs<>();
    }

    /**
     * 缓存策略
     * @since 1.0.0
     */
    private ICache<K,V> cache = Caches.defaults();

    /**
     * map 实现
     * @since 0.0.2
     */
    private ICacheMap<K, V> map = CacheMaps.defaults();

    /**
     * 大小限制
     * @since 0.0.2
     */
    private int size = Integer.MAX_VALUE;

    /**
     * 驱除策略
     * @since 0.0.2
     */
    private ICacheEvict<K,V> evict = CacheEvicts.defaults();

    /**
     * 加载策略
     * @since 0.0.7
     */
    private ICacheLoad<K,V> load = CacheLoads.defaults();

    /**
     * 持久化实现策略
     * @since 0.0.8
     */
    private ICachePersist<K,V> persist = CachePersists.defaults();

    /**
     * 内部的淘汰策略
     * @since 1.0.0
     */
    private ICacheExpire<K, V> expire = CacheExpires.defaults();

    /**
     * 操作监听类
     *
     * @since 1.0.0
     */
    private List<ICacheInterceptor<K,V>> interceptorList = CacheInterceptors.defaults();

    /**
     * map 实现
     * @param map map
     * @return this
     * @since 0.0.2
     */
    public CacheBs<K, V> map(ICacheMap<K, V> map) {
        ArgUtil.notNull(map, "map");

        this.map = map;
        return this;
    }

    /**
     * 设置 size 信息
     * @param size size
     * @return this
     * @since 0.0.2
     */
    public CacheBs<K, V> size(int size) {
        ArgUtil.notNegative(size, "size");

        this.size = size;
        return this;
    }

    /**
     * 设置驱除策略
     * @param evict 驱除策略
     * @return this
     * @since 0.0.2
     */
    public CacheBs<K, V> evict(ICacheEvict<K, V> evict) {
        ArgUtil.notNull(evict, "evict");

        this.evict = evict;
        return this;
    }

    /**
     * 设置加载
     * @param load 加载
     * @return this
     * @since 0.0.7
     */
    public CacheBs<K, V> load(ICacheLoad<K, V> load) {
        ArgUtil.notNull(load, "load");

        this.load = load;
        return this;
    }

    /**
     * 设置持久化策略
     * @param persist 持久化
     * @return this
     * @since 0.0.8
     */
    public CacheBs<K, V> persist(ICachePersist<K, V> persist) {
        ArgUtil.notNull(persist, "persist");

        this.persist = persist;
        return this;
    }

    /**
     * 过期策略
     * @param expire 策略
     * @return 结果
     * @since 1.0.0
     */
    public CacheBs<K, V> expire(ICacheExpire<K, V> expire) {
        ArgUtil.notNull(expire, "expire");

        this.expire = expire;
        return this;
    }

    /**
     * 操作监听类列表
     * @param interceptorList 策略
     * @return 结果
     * @since 1.0.0
     */
    public CacheBs<K, V> interceptorList(List<ICacheInterceptor<K,V>> interceptorList) {
        this.interceptorList = interceptorList;
        return this;
    }

    /**
     * 缓存类
     * @param cache 策略
     * @return 结果
     * @since 1.0.0
     */
    public CacheBs<K, V> cache(ICache<K, V> cache) {
        ArgUtil.notNull(cache, "cache");

        this.cache = cache;
        return this;
    }

    /**
     * 构建缓存信息
     * @return 缓存信息
     * @since 0.0.2
     */
    public ICache<K,V> build() {
        CacheContext<K,V> tempCacheContext = new CacheContext<>();

        // 排序
        Collections.sort(interceptorList, new Comparator<ICacheInterceptor<K, V>>() {
            @Override
            public int compare(ICacheInterceptor<K, V> o1, ICacheInterceptor<K, V> o2) {
                return o1.order() - o2.order();
            }
        });

        tempCacheContext.map(map);
        tempCacheContext.evict(evict);
        tempCacheContext.size(size);
        tempCacheContext.load(load);
        tempCacheContext.persist(persist);
        tempCacheContext.interceptorList(interceptorList);

        // 初始化 expire
        CacheExpireContext<K,V> expireContext = new CacheExpireContext<>();
        expireContext.map(map);
        this.expire.init(expireContext);

        // 初始化 persist
        CachePersistContext<K,V> persistContext = new CachePersistContext<>();
        persistContext.map(map);
        persistContext.expire(expire);
        this.persist.init(persistContext);

        // 初始化 load
        CacheLoadContext<K,V> loadContext = new CacheLoadContext<>();
        loadContext.map(map);
        loadContext.expire(expire);
        this.load.init(loadContext);

        // 初始化 cache
        tempCacheContext.expire(expire);
        tempCacheContext.persist(persist);

        this.cache.init(tempCacheContext);

        // 执行数据 load
        this.load.load();

        // 初始化
        return cache;
    }

}
