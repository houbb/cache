package com.github.houbb.cache.core.support.evict.impl;

import com.github.houbb.cache.api.ICacheContext;
import com.github.houbb.cache.api.ICacheEntry;
import com.github.houbb.cache.core.model.CacheEntry;
import com.github.houbb.cache.core.support.evict.AbstractCacheEvict;
import com.github.houbb.cache.core.support.struct.lru.ILruMap;
import com.github.houbb.cache.core.support.struct.lru.impl.LruMapCircleList;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

/**
 * 淘汰策略-clock 算法
 *
 * @author binbin.hou
 * @since 0.0.15
 */
public class CacheEvictClock<K,V> extends AbstractCacheEvict<K,V> {

    private static final Log log = LogFactory.getLog(CacheEvictClock.class);

    /**
     * 循环链表
     * @since 0.0.15
     */
    private final ILruMap<K,V> circleList;

    public CacheEvictClock() {
        this.circleList = new LruMapCircleList<>();
    }

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheContext<K, V> context, final K newKey) {
        ICacheEntry<K, V> result = null;

        // 超过限制，移除队尾的元素
        if(isNeedEvict(context)) {
            ICacheEntry<K,V>  evictEntry = circleList.removeEldest();;
            // 执行缓存移除操作
            final K evictKey = evictEntry.key();
            V evictValue = doEvictRemove(context, evictKey);

            log.debug("基于 clock 算法淘汰 key：{}, value: {}", evictKey, evictValue);
            result = new CacheEntry<>(evictKey, evictValue);
        }

        return result;
    }


    /**
     * 更新信息
     * @param key 元素
     * @since 0.0.15
     */
    @Override
    public void updateKey(ICacheContext<K, V> context, final K key) {
        this.circleList.updateKey(key);
    }

    /**
     * 移除元素
     *
     * @param key 元素
     * @since 0.0.15
     */
    @Override
    public void removeKey(ICacheContext<K, V> context, final K key) {
        this.circleList.removeKey(key);
    }

}
