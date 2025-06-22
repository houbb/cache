package com.github.houbb.cache.core.core;

import com.github.houbb.cache.api.*;

import java.util.List;

/**
 * 缓存上下文
 * @author binbin.hou
 * @since 0.0.2
 */
public class CacheContext<K,V> implements ICacheContext<K, V> {

    /**
     * map 信息
     * @since 0.0.2
     */
    private ICacheMap<K, V> map;

    /**
     * 大小限制
     * @since 0.0.2
     */
    private int size;

    /**
     * 驱除策略
     * v1.0.0 重命名为 evict
     * @since 1.0.0 重命名为 evict
     */
    private ICacheEvict<K,V> evict;

    /**
     * 过期策略
     * @since 1.0.0
     */
    private ICacheExpire<K,V> expire;

    /**
     * 加载策略
     * @since 1.0.0
     */
    private ICacheLoad<K,V> load;

    /**
     * 持久化策略
     * @since 1.0.0
     */
    private ICachePersist<K,V> persist;

    /**
     * 操作监听类
     * @since 1.0.0
     */
    private List<ICacheInterceptor<K,V>> interceptorList;

    @Override
    public ICacheMap<K, V> map() {
        return map;
    }

    public CacheContext<K, V> map(ICacheMap<K, V> map) {
        this.map = map;
        return this;
    }

    @Override
    public int size() {
        return size;
    }

    public CacheContext<K, V> size(int size) {
        this.size = size;
        return this;
    }

    @Override
    public ICacheEvict<K, V> evict() {
        return evict;
    }

    public CacheContext<K, V> evict(ICacheEvict<K, V> evict) {
        this.evict = evict;
        return this;
    }

    @Override
    public ICacheExpire<K, V> expire() {
        return expire;
    }

    public CacheContext<K, V> expire(ICacheExpire<K, V> expire) {
        this.expire = expire;
        return this;
    }

    @Override
    public ICacheLoad<K, V> load() {
        return load;
    }

    public CacheContext<K, V> load(ICacheLoad<K, V> load) {
        this.load = load;
        return this;
    }

    @Override
    public ICachePersist<K, V> persist() {
        return persist;
    }

    public CacheContext<K, V> persist(ICachePersist<K, V> persist) {
        this.persist = persist;
        return this;
    }

    @Override
    public List<ICacheInterceptor<K, V>> interceptorList() {
        return interceptorList;
    }

    public CacheContext<K, V> interceptorList(List<ICacheInterceptor<K, V>> interceptorList) {
        this.interceptorList = interceptorList;
        return this;
    }

}
