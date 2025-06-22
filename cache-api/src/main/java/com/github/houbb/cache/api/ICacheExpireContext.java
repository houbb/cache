package com.github.houbb.cache.api;

/**
 * 缓存过期上下文
 *
 * @author binbin.hou
 * @since 1.0.0
 */
public interface ICacheExpireContext<K, V> {

    /**
     * map 信息
     * @return map
     * @since 0.0.2
     */
    ICacheMap<K, V> map();

}
