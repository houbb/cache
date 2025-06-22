package com.github.houbb.cache.api;

/**
 * 持久化缓存接口
 * @author binbin.hou
 * @since 1.0.0
 * @param <K> key
 * @param <V> value
 */
public interface ICachePersistContext<K, V> {

    /**
     * map 信息
     * @return map
     */
    ICacheMap<K, V> map();

    /**
     * 过期策略
     * @return 实现
     */
    ICacheExpire<K, V> expire();

}
