package com.github.houbb.cache.core.support.evict.impl;

import com.github.houbb.cache.api.ICacheContext;
import com.github.houbb.cache.core.model.CacheEntry;
import com.github.houbb.cache.core.support.evict.AbstractCacheEvict;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 丢弃策略-先进先出
 * @author binbin.hou
 * @since 0.0.2
 */
public class CacheEvictFifo<K,V> extends AbstractCacheEvict<K,V> {

    /**
     * queue 信息
     * @since 0.0.2
     */
    private final Queue<K> queue = new LinkedList<>();

    @Override
    public CacheEntry<K,V> doEvict(ICacheContext<K, V> context, final K newKey) {
        CacheEntry<K,V> result = null;

        // 超过限制，执行移除
        if(isNeedEvict(context)) {
            K evictKey = queue.remove();
            // 移除最开始的元素
            V evictValue = doEvictRemove(context, evictKey);
            result = new CacheEntry<>(evictKey, evictValue);
        }

        return result;
    }

    @Override
    public void updateKey(ICacheContext<K, V> context, K key) {
        // 将新加的元素放入队尾
        queue.add(key);
    }

}
