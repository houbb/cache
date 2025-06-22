package com.github.houbb.cache.core.support.map.impl;


import com.github.houbb.cache.core.support.map.AbstractCacheMap;

import java.util.HashMap;

/**
 * hashMap 实现
 *
 * @see HashMap
 * @since 1.0.0
 */
public class CacheHashMap<K,V> extends AbstractCacheMap<K,V> {

    @Override
    protected void initMap() {
        innerMap = new HashMap<>();
    }

}
