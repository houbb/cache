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
     * 初始化上下文
     * @param persistContext 上下文
     * @return this
     */
    ICachePersist<K, V> init(final ICachePersistContext<K,V> persistContext);

}
