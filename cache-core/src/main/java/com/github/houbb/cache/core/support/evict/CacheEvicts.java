package com.github.houbb.cache.core.support.evict;

import com.github.houbb.cache.api.ICacheEvict;

/**
 * 丢弃策略
 *
 * @author binbin.hou
 * @since 0.0.2
 */
public final class CacheEvicts {

    private CacheEvicts(){}

    /**
     * 无策略
     *
     * @param <K> key
     * @param <V> value
     * @return 结果
     * @since 0.0.2
     */
    public static <K, V> ICacheEvict<K, V> none() {
        return new CacheEvictNone<>();
    }

    /**
     * 先进先出
     *
     * @param <K> key
     * @param <V> value
     * @return 结果
     * @since 0.0.2
     */
    public static <K, V> ICacheEvict<K, V> fifo() {
        return new CacheEvictFIFO<>();
    }

    /**
     * LRU 驱除策略
     *
     * @param <K> key
     * @param <V> value
     * @return 结果
     * @since 0.0.11
     */
    public static <K, V> ICacheEvict<K, V> lru() {
        return new CacheEvictLRU<>();
    }

}
