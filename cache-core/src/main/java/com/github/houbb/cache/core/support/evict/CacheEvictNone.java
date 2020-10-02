package com.github.houbb.cache.core.support.evict;

import com.github.houbb.cache.api.ICacheEntry;
import com.github.houbb.cache.api.ICacheEvict;
import com.github.houbb.cache.api.ICacheEvictContext;

/**
 * 丢弃策略
 * @author binbin.hou
 * @since 0.0.2
 */
public class CacheEvictNone<K,V> extends AbstractCacheEvict<K,V> {

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        return null;
    }

}
