package com.github.houbb.cache.core.support.map;

import com.github.houbb.cache.api.ICacheMap;
import com.github.houbb.cache.core.support.map.impl.CacheConcurrentHashMap;
import com.github.houbb.cache.core.support.map.impl.CacheHashMap;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author binbin.hou
 * @since 0.0.3
 */
public final class CacheMaps {

    private CacheMaps(){}

    /**
     * 默认实现策略
     * @param <K> key
     * @param <V> value
     * @return map 实现
     * @since 1.0.0
     */
    public static <K,V> ICacheMap<K,V> defaults() {
        return concurrentHashMap();
    }

    /**
     * hashMap 实现策略
     * @param <K> key
     * @param <V> value
     * @return map 实现
     * @since 0.0.3
     */
    public static <K,V> ICacheMap<K,V> hashMap() {
        return new CacheHashMap<>();
    }

    /**
     * concurrentHashMap 实现策略
     * @param <K> key
     * @param <V> value
     * @return map 实现
     * @since 1.0.0
     */
    public static <K,V> ICacheMap<K,V> concurrentHashMap() {
        return new CacheConcurrentHashMap<>();
    }

}
