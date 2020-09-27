package com.github.houbb.cache.core.support.evict;

import com.github.houbb.cache.api.ICache;
import com.github.houbb.cache.api.ICacheEvictContext;

/**
 * 驱除策略
 *
 * 1. 新加的 key
 * 2. map 实现
 * 3. 淘汰监听器
 *
 * @author binbin.hou
 * @since 0.0.2
 * @param <K> key
 * @param <V> value
 */
public class CacheEvictContext<K,V> implements ICacheEvictContext<K,V> {

    /**
     * 新加的 key
     * @since 0.0.2
     */
    private K key;

    /**
     * cache 实现
     * @since 0.0.2
     */
    private ICache<K,V> cache;

    /**
     * 最大的大小
     * @since 0.0.2
     */
    private int size;

    @Override
    public K key() {
        return key;
    }

    public CacheEvictContext<K, V> key(K key) {
        this.key = key;
        return this;
    }

    @Override
    public ICache<K, V> cache() {
        return cache;
    }

    public CacheEvictContext<K, V> cache(ICache<K, V> cache) {
        this.cache = cache;
        return this;
    }

    @Override
    public int size() {
        return size;
    }

    public CacheEvictContext<K, V> size(int size) {
        this.size = size;
        return this;
    }
}
