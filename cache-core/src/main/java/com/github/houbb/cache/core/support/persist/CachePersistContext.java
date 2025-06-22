package com.github.houbb.cache.core.support.persist;

import com.github.houbb.cache.api.ICacheExpire;
import com.github.houbb.cache.api.ICacheMap;
import com.github.houbb.cache.api.ICachePersistContext;

/**
 * @since 1.0.0
 * @param <K> key
 * @param <V> value
 */
public class CachePersistContext<K,V> implements ICachePersistContext<K,V> {

    /**
     * map 信息
     */
    private ICacheMap<K, V> map;

    /**
     * 过期策略
     */
    private ICacheExpire<K, V> expire;

    @Override
    public ICacheMap<K, V> map() {
        return map;
    }

    public CachePersistContext<K, V> map(ICacheMap<K, V> map) {
        this.map = map;
        return this;
    }

    @Override
    public ICacheExpire<K, V> expire() {
        return expire;
    }

    public CachePersistContext<K, V> expire(ICacheExpire<K, V> expire) {
        this.expire = expire;
        return this;
    }

}
