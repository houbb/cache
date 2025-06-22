package com.github.houbb.cache.core.support.evict.impl;

import com.github.houbb.cache.api.ICacheContext;
import com.github.houbb.cache.api.ICacheEntry;
import com.github.houbb.cache.core.model.CacheEntry;
import com.github.houbb.cache.core.support.evict.AbstractCacheEvict;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * 丢弃策略-LRU 最近最少使用
 * @author binbin.hou
 * @since 0.0.11
 */
public class CacheEvictLru<K,V> extends AbstractCacheEvict<K,V> {

    private static final Log log = LogFactory.getLog(CacheEvictLru.class);

    /**
     * list 信息
     * @since 0.0.11
     */
    private final List<K> list = new LinkedList<>();

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheContext<K, V> context, final K newKey) {
        ICacheEntry<K, V> result = null;
        // 超过限制，移除队尾的元素
        if(isNeedEvict(context)) {
            K evictKey = list.get(list.size()-1);
            V evictValue = doEvictRemove(context, evictKey);
            result = new CacheEntry<>(evictKey, evictValue);
        }

        return result;
    }


    /**
     * 放入元素
     * （1）删除已经存在的
     * （2）新元素放到元素头部
     *
     * @param key 元素
     * @since 0.0.11
     */
    @Override
    public void updateKey(ICacheContext<K, V> context, final K key) {
        this.list.remove(key);
        this.list.add(0, key);
    }

    /**
     * 移除元素
     * @param key 元素
     * @since 0.0.11
     */
    @Override
    public void removeKey(ICacheContext<K, V> context, final K key) {
        this.list.remove(key);
    }

}
