package com.github.houbb.cache.core.core;

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
    private final Map<K,V> map;

    /**
     * 大小限制
     * @since 0.0.2
     */
    private final int sizeLimit;

    /**
     * 驱除策略
     * @since 0.0.2
     */
    private final ICacheEvict<K,V> cacheEvict;

    /**
     * 过期策略
     * 暂时不做暴露
     * @since 0.0.3
     */
    private final ICacheExpire<K,V> cacheExpire;

    public Cache(ICacheContext<K, V> context) {
        this.map = context.map();
        this.sizeLimit = context.size();
        this.cacheEvict = context.cacheEvict();
        this.cacheExpire = new CacheExpire<>(this);
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
    public int size() {
        //1. 刷新所有过期信息
        this.refreshExpireAllKeys();

        return map.size();
    }

    @Override
    public boolean isEmpty() {
        //1. 刷新所有过期信息
        this.refreshExpireAllKeys();

        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        //1. 刷新所有过期信息
        this.refreshExpireAllKeys();

        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        //1. 刷新所有过期信息
        this.refreshExpireAllKeys();

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
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        this.refreshExpireAllKeys();

        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        this.refreshExpireAllKeys();

        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        this.refreshExpireAllKeys();

        return map.entrySet();
    }

    /**
     * 刷新懒过期处理所有的 keys
     * @since 0.0.3
     */
    private void refreshExpireAllKeys() {
        this.cacheExpire.refreshExpire(map.keySet());
    }

}
