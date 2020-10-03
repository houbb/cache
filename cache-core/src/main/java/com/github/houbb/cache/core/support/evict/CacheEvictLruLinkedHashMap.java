package com.github.houbb.cache.core.support.evict;

import com.github.houbb.cache.api.ICache;
import com.github.houbb.cache.api.ICacheEntry;
import com.github.houbb.cache.api.ICacheEvict;
import com.github.houbb.cache.api.ICacheEvictContext;
import com.github.houbb.cache.core.model.CacheEntry;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 丢弃策略-LRU 最近最少使用
 *
 * 实现方式：LinkedHashMap
 * @author binbin.hou
 * @since 0.0.12
 */
public class CacheEvictLruLinkedHashMap<K,V> extends LinkedHashMap<K,V>
    implements ICacheEvict<K,V> {

    private static final Log log = LogFactory.getLog(CacheEvictLruDoubleListMap.class);

    /**
     * 是否移除标识
     * @since 0.0.12
     */
    private volatile boolean removeFlag = false;

    /**
     * 最旧的一个元素
     * @since 0.0.12
     */
    private transient Map.Entry<K, V> eldest = null;

    public CacheEvictLruLinkedHashMap() {
        super(16, 0.75f, true);
    }

    @Override
    public ICacheEntry<K, V> evict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K, V> result = null;
        final ICache<K,V> cache = context.cache();
        // 超过限制，移除队尾的元素
        if(cache.size() >= context.size()) {
            removeFlag = true;

            // 执行 put 操作
            super.put(context.key(), null);

            // 构建淘汰的元素
            K evictKey = eldest.getKey();
            V evictValue = cache.remove(evictKey);
            result = new CacheEntry<>(evictKey, evictValue);
        } else {
            removeFlag = false;
        }

        return result;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        this.eldest = eldest;
        return removeFlag;
    }

    @Override
    public void updateKey(K key) {
        super.put(key, null);
    }

    @Override
    public void removeKey(K key) {
        super.remove(key);
    }

}
