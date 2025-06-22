package com.github.houbb.cache.core.support.map.impl;


import com.github.houbb.cache.core.support.map.AbstractCacheMap;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentHashMap 实现
 *
 * @see ConcurrentHashMap
 * @since 1.0.0
 */
public class CacheConcurrentHashMap<K,V> extends AbstractCacheMap<K,V> {

    @Override
    protected void initMap() {
        innerMap = new ConcurrentHashMap<>();
    }

}
