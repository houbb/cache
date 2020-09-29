package com.github.houbb.cache.core.support.evict;

import com.github.houbb.cache.api.ICacheEvict;
import com.github.houbb.cache.api.ICacheEvictContext;

/**
 * 丢弃策略
 * @author binbin.hou
 * @since 0.0.2
 */
public class CacheEvictNone<K,V> implements ICacheEvict<K,V> {

    @Override
    public boolean evict(ICacheEvictContext<K, V> context) {
        return false;
    }

}
