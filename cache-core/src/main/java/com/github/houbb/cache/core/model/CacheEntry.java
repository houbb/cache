package com.github.houbb.cache.core.model;

import com.github.houbb.cache.api.ICacheEntry;

/**
 * key value 的明细信息
 * @author binbin.hou
 * @since 0.0.11
 * @param <K> key
 * @param <V> value
 */
public class CacheEntry<K,V> implements ICacheEntry<K,V> {

    /**
     * key
     * @since 0.0.11
     */
    private final K key;

    /**
     * value
     * @since 0.0.11
     */
    private final V value;

    /**
     * 新建元素
     * @param key key
     * @param value value
     * @param <K> 泛型
     * @param <V> 泛型
     * @return 结果
     * @since 0.0.11
     */
    public static <K,V> CacheEntry<K,V> of(final K key,
                                           final V value) {
        return new CacheEntry<>(key, value);
    }

    public CacheEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K key() {
        return key;
    }

    @Override
    public V value() {
        return value;
    }

    @Override
    public String toString() {
        return "EvictEntry{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }

}
