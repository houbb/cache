package com.github.houbb.cache.core.support.persist;

import com.github.houbb.cache.api.ICache;
import com.github.houbb.cache.api.ICachePersist;

/**
 * 缓存持久化-无任何操作
 * @author binbin.hou
 * @since 0.0.8
 */
public class CachePersistNone<K,V> implements ICachePersist<K,V> {

    /**
     * 持久化
     * @param cache 缓存
     */
    @Override
    public void persist(ICache<K, V> cache) {
    }

}
