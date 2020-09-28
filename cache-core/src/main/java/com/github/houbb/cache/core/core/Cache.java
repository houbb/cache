package com.github.houbb.cache.core.core;

import com.github.houbb.cache.annotation.Refresh;
import com.github.houbb.cache.api.*;
import com.github.houbb.cache.core.exception.CacheRuntimeException;
import com.github.houbb.cache.core.support.evict.CacheEvictContext;
import com.github.houbb.cache.core.support.expire.CacheExpire;

import java.util.*;

/**
 * 缓存信息
 *
 * @author binbin.hou
 * @param <K> key
 * @param <V> value
 * @since 0.0.2
 */
public class Cache<K,V> implements ICache<K,V> {

    /**
     * map 信息
     * @since 0.0.2
     */
    private Map<K,V> map;

    /**
     * 大小限制
     * @since 0.0.2
     */
    private int sizeLimit;

    /**
     * 驱除策略
     * @since 0.0.2
     */
    private ICacheEvict<K,V> cacheEvict;

    /**
     * 过期策略
     * 暂时不做暴露
     * @since 0.0.3
     */
    private ICacheExpire<K,V> cacheExpire = new CacheExpire<>(this);

    public Cache<K, V> map(Map<K, V> map) {
        this.map = map;
        return this;
    }

    public Cache<K, V> sizeLimit(int sizeLimit) {
        this.sizeLimit = sizeLimit;
        return this;
    }

    public Cache<K, V> cacheEvict(ICacheEvict<K, V> cacheEvict) {
        this.cacheEvict = cacheEvict;
        return this;
    }

    @Override
    public ICache<K, V> expire(K key, long timeInMills) {
        long expireTime = System.currentTimeMillis() + timeInMills;
        return this.expireAt(key, expireTime);
    }

    @Override
    public ICache<K, V> expireAt(K key, long timeInMills) {
        this.cacheExpire.expire(key, timeInMills);
        return this;
    }

    @Override
    public ICacheExpire<K, V> cacheExpire() {
        return this.cacheExpire;
    }

    @Override
    @Refresh
    public int size() {
        return map.size();
    }

    @Override
    @Refresh
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    @Refresh
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    @Refresh
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public V get(Object key) {
        //1. 刷新所有过期信息
        K genericKey = (K) key;
        this.cacheExpire.refreshExpire(Collections.singletonList(genericKey));

        return map.get(key);
    }

    @Override
    public V put(K key, V value) {
        //1.1 尝试驱除
        CacheEvictContext<K,V> context = new CacheEvictContext<>();
        context.key(key).size(sizeLimit).cache(this);
        cacheEvict.evict(context);

        //2. 判断驱除后的信息
        if(isSizeLimit()) {
            throw new CacheRuntimeException("当前队列已满，数据添加失败！");
        }

        //3. 执行添加
        return map.put(key, value);
    }

    /**
     * 是否已经达到大小最大的限制
     * @return 是否限制
     * @since 0.0.2
     */
    private boolean isSizeLimit() {
        final int currentSize = this.size();
        return currentSize >= this.sizeLimit;
    }

    @Override
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    @Refresh
    public void clear() {
        map.clear();
    }

    @Override
    @Refresh
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    @Refresh
    public Collection<V> values() {
        return map.values();
    }

    @Override
    @Refresh
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

}
