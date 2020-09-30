package com.github.houbb.cache.api;

/**
 * 持久化缓存接口
 * @author binbin.hou
 * @since 0.0.8
 * @param <K> key
 * @param <V> value
 */
public interface ICachePersist<K, V> {

    /**
     * 持久化缓存信息
     * @param cache 缓存
     * @since 0.0.7
     */
    void persist(final ICache<K, V> cache);

}
