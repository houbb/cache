package com.github.houbb.cache.core.core;

import com.github.houbb.cache.annotation.CacheInterceptor;
import com.github.houbb.cache.api.*;
import com.github.houbb.cache.core.constant.enums.CacheRemoveType;
import com.github.houbb.cache.core.exception.CacheRuntimeException;
import com.github.houbb.cache.core.support.evict.CacheEvictContext;
import com.github.houbb.cache.core.support.expire.CacheExpire;
import com.github.houbb.cache.core.support.listener.CacheRemoveListenerContext;

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

    /**
     * 删除监听类
     * @since 0.0.6
     */
    private List<ICacheRemoveListener<K,V>> removeListeners;

    /**
     * 设置 map 实现
     * @param map 实现
     * @return this
     */
    public Cache<K, V> map(Map<K, V> map) {
        this.map = map;
        return this;
    }

    /**
     * 设置大小限制
     * @param sizeLimit 大小限制
     * @return this
     */
    public Cache<K, V> sizeLimit(int sizeLimit) {
        this.sizeLimit = sizeLimit;
        return this;
    }

    /**
     * 设置驱除策略
     * @param cacheEvict 驱除策略
     * @return this
     */
    public Cache<K, V> cacheEvict(ICacheEvict<K, V> cacheEvict) {
        this.cacheEvict = cacheEvict;
        return this;
    }

    @Override
    public List<ICacheRemoveListener<K, V>> removeListeners() {
        return removeListeners;
    }

    public Cache<K, V> removeListeners(List<ICacheRemoveListener<K, V>> removeListeners) {
        this.removeListeners = removeListeners;
        return this;
    }

    /**
     * 设置过期策略
     * @param key         key
     * @param timeInMills 毫秒时间之后过期
     * @return this
     */
    @Override
    @CacheInterceptor
    public ICache<K, V> expire(K key, long timeInMills) {
        long expireTime = System.currentTimeMillis() + timeInMills;
        return this.expireAt(key, expireTime);
    }

    @Override
    @CacheInterceptor
    public ICache<K, V> expireAt(K key, long timeInMills) {
        this.cacheExpire.expire(key, timeInMills);
        return this;
    }

    @Override
    @CacheInterceptor
    public ICacheExpire<K, V> cacheExpire() {
        return this.cacheExpire;
    }

    @Override
    @CacheInterceptor(refresh = true)
    public int size() {
        return map.size();
    }

    @Override
    @CacheInterceptor(refresh = true)
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    @CacheInterceptor(refresh = true)
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    @CacheInterceptor(refresh = true)
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    @CacheInterceptor
    @SuppressWarnings("unchecked")
    public V get(Object key) {
        //1. 刷新所有过期信息
        K genericKey = (K) key;
        this.cacheExpire.refreshExpire(Collections.singletonList(genericKey));

        return map.get(key);
    }

    @Override
    @CacheInterceptor
    public V put(K key, V value) {
        //1.1 尝试驱除
        CacheEvictContext<K,V> context = new CacheEvictContext<>();
        context.key(key).size(sizeLimit).cache(this);
        boolean evictResult = cacheEvict.evict(context);
        if(evictResult) {
            // 执行淘汰监听器
            ICacheRemoveListenerContext<K,V> removeListenerContext = CacheRemoveListenerContext.<K,V>newInstance().key(key).value(value).type(CacheRemoveType.EVICT.code());
            for(ICacheRemoveListener<K,V> listener : this.removeListeners) {
                listener.listen(removeListenerContext);
            }
        }

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
    @CacheInterceptor
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    @CacheInterceptor
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    @CacheInterceptor(refresh = true)
    public void clear() {
        map.clear();
    }

    @Override
    @CacheInterceptor(refresh = true)
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    @CacheInterceptor(refresh = true)
    public Collection<V> values() {
        return map.values();
    }

    @Override
    @CacheInterceptor(refresh = true)
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

}
