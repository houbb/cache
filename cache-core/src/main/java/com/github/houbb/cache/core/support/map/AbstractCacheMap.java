package com.github.houbb.cache.core.support.map;

import com.github.houbb.cache.api.ICacheMap;

import java.util.Map;
import java.util.Set;

/**
 * @since 1.0.0
 * @param <K> key
 * @param <V> value
 */
public abstract class AbstractCacheMap<K,V> implements ICacheMap<K,V> {

    // 内部 map，暂时偷懒基于 map 实现，后续可以调整为自己的实现
    protected Map<K,V> innerMap;

    protected abstract void initMap();

    public AbstractCacheMap() {
        this.initMap();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public int size() {
        return innerMap.size();
    }

    @Override
    public boolean containsKey(K key) {
        return innerMap.containsKey(key);
    }

    @Override
    public V get(K key) {
        return innerMap.get(key);
    }

    @Override
    public V put(K key, V value) {
        return innerMap.put(key, value);
    }

    @Override
    public V remove(K key) {
        return innerMap.remove(key);
    }

    @Override
    public Set<K> keySet() {
        return innerMap.keySet();
    }

}
