package com.github.houbb.cache.core.support.persist;

import com.github.houbb.cache.api.ICache;
import com.github.houbb.cache.api.ICachePersist;

import java.util.concurrent.TimeUnit;

/**
 * 缓存持久化-适配器模式
 * @author binbin.hou
 * @since 0.0.10
 */
public class CachePersistAdaptor<K,V> implements ICachePersist<K,V> {

    /**
     * 持久化
     * key长度 key+value
     * 第一个空格，获取 key 的长度，然后截取
     * @param cache 缓存
     */
    @Override
    public void persist(ICache<K, V> cache) {
    }

    @Override
    public long delay() {
        return this.period();
    }

    @Override
    public long period() {
        return 1;
    }

    @Override
    public TimeUnit timeUnit() {
        return TimeUnit.SECONDS;
    }

}
