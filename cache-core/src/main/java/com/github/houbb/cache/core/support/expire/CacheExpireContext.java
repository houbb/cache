package com.github.houbb.cache.core.support.expire;

import com.github.houbb.cache.api.ICacheExpireContext;
import com.github.houbb.cache.api.ICacheMap;

/**
 * 缓存过期上下文
 * @author binbin.hou
 * @since 1.0.0
 */
public class CacheExpireContext<K,V> implements ICacheExpireContext<K, V> {

    /**
     * map 信息
     * @since 1.0.0
     */
    private ICacheMap<K, V> map;

    @Override
    public ICacheMap<K, V> map() {
        return map;
    }

    public CacheExpireContext<K, V> map(ICacheMap<K, V> map) {
        this.map = map;
        return this;
    }

}
