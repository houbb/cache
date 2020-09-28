package com.github.houbb.cache.core.bs;

import com.github.houbb.cache.api.ICache;
import com.github.houbb.cache.api.ICacheEvict;
import com.github.houbb.cache.api.ICacheInterceptor;
import com.github.houbb.cache.core.core.Cache;
import com.github.houbb.cache.core.core.CacheContext;
import com.github.houbb.cache.core.support.evict.CacheEvicts;
import com.github.houbb.cache.core.support.interceptor.CacheInterceptors;
import com.github.houbb.cache.core.support.proxy.CacheProxy;
import com.github.houbb.heaven.util.common.ArgUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缓存引导类
 * @author binbin.hou
 * @since 0.0.2
 */
public final class CacheBs<K,V> {

    private CacheBs(){}

    /**
     * 创建对象实例
     * @param <K> key
     * @param <V> value
     * @return this
     * @since 0.0.2
     */
    public static <K,V> CacheBs<K,V> newInstance() {
        return new CacheBs<>();
    }

    /**
     * map 实现
     * @since 0.0.2
     */
    private Map<K,V> map = new HashMap<>();

    /**
     * 大小限制
     * @since 0.0.2
     */
    private int size = Integer.MAX_VALUE;

    /**
     * 驱除策略
     * @since 0.0.2
     */
    private ICacheEvict<K,V> evict = CacheEvicts.fifo();

    /**
     * map 实现
     * @param map map
     * @return this
     * @since 0.0.2
     */
    public CacheBs<K, V> map(Map<K, V> map) {
        ArgUtil.notNull(map, "map");

        this.map = map;
        return this;
    }

    /**
     * 设置 size 信息
     * @param size size
     * @return this
     * @since 0.0.2
     */
    public CacheBs<K, V> size(int size) {
        ArgUtil.notNegative(size, "size");

        this.size = size;
        return this;
    }

    /**
     * 设置驱除策略
     * @param evict 驱除策略
     * @return this
     * @since 0.0.2
     */
    public CacheBs<K, V> evict(ICacheEvict<K, V> evict) {
        this.evict = evict;
        return this;
    }

    /**
     * 构建缓存信息
     * @return 缓存信息
     * @since 0.0.2
     */
    public ICache<K,V> build() {
        Cache<K,V> cache = new Cache<>();
        cache.map(map);
        cache.cacheEvict(evict);
        cache.sizeLimit(size);

        return CacheProxy.getProxy(cache);
    }

}
