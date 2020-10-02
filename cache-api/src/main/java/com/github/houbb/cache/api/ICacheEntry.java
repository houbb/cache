package com.github.houbb.cache.api;

/**
 * 缓存明细信息
 * @author binbin.hou
 * @since 0.0.11
 * @param <K> key
 * @param <V> value
 */
public interface ICacheEntry<K, V> {

    /**
     * @since 0.0.11
     * @return key
     */
    K key();

    /**
     * @since 0.0.11
     * @return value
     */
    V value();

}
