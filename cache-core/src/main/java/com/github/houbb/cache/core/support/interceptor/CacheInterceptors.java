package com.github.houbb.cache.core.support.interceptor;

import com.github.houbb.cache.api.ICacheInterceptor;
import com.github.houbb.cache.core.support.interceptor.aof.CacheInterceptorAof;
import com.github.houbb.cache.core.support.interceptor.common.CacheInterceptorCommonCost;
import com.github.houbb.cache.core.support.interceptor.evict.CacheInterceptorEvict;
import com.github.houbb.cache.core.support.interceptor.evict.CacheInterceptorEvictRemove;
import com.github.houbb.cache.core.support.interceptor.evict.CacheInterceptorEvictUpdate;
import com.github.houbb.cache.core.support.interceptor.refresh.CacheInterceptorRefresh;

import java.util.ArrayList;
import java.util.List;

/**
 * 缓存拦截器工具类
 * @author binbin.hou
 * @since 0.0.5
 */
public final class CacheInterceptors {

    /**
     * 默认通用
     * @return 结果
     * @since 0.0.5
     * @param <K> key
     * @param <V> value
     */
    @SuppressWarnings("all")
    public static <K,V> List<ICacheInterceptor<K,V>> defaults() {
        List<ICacheInterceptor<K,V>> list = new ArrayList<>();
        list.add(CacheInterceptors.<K, V>commonCost());
        list.add(CacheInterceptors.<K, V>evict());
        list.add(CacheInterceptors.<K, V>evictUpdate());
        list.add(CacheInterceptors.<K, V>evictRemove());
        list.add(CacheInterceptors.<K, V>aof());
        list.add(CacheInterceptors.<K, V>refresh());
        return list;
    }

    public static <K,V> ICacheInterceptor<K,V> commonCost() {
        return new CacheInterceptorCommonCost<>();
    }

    public static <K,V> ICacheInterceptor<K,V> evict() {
        return new CacheInterceptorEvict<>();
    }

    /**
     * 更新
     * @return 实现
     * @param <K> key
     * @param <V> value
     * @since 1.0.1
     */
    public static <K,V> ICacheInterceptor<K,V> evictUpdate() {
        return new CacheInterceptorEvictUpdate<>();
    }

    /**
     * 删除
     * @return 实现
     * @param <K> key
     * @param <V> value
     * @since 1.0.1
     */
    public static <K,V> ICacheInterceptor<K,V> evictRemove() {
        return new CacheInterceptorEvictRemove<>();
    }

    public static <K,V> ICacheInterceptor<K,V> aof() {
        return new CacheInterceptorAof<>();
    }

    public static <K,V> ICacheInterceptor<K,V> refresh() {
        return new CacheInterceptorRefresh<>();
    }


}
