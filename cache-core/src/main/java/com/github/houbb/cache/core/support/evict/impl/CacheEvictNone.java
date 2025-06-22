package com.github.houbb.cache.core.support.evict.impl;

import com.github.houbb.cache.api.ICacheContext;
import com.github.houbb.cache.api.ICacheEntry;
import com.github.houbb.cache.core.support.evict.AbstractCacheEvict;

/**
 * 丢弃策略
 * @author binbin.hou
 * @since 0.0.2
 */
public class CacheEvictNone<K,V> extends AbstractCacheEvict<K,V> {

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheContext<K, V> context, K evictKey) {
        return null;
    }

}
