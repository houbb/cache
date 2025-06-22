package com.github.houbb.cache.core.support.interceptor;

import com.github.houbb.cache.api.ICacheInterceptor;
import com.github.houbb.cache.core.support.interceptor.aof.CacheInterceptorAof;
import com.github.houbb.cache.core.support.interceptor.common.CacheInterceptorCommonCost;
import com.github.houbb.cache.core.support.interceptor.evict.CacheInterceptorEvict;
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
     */
    @SuppressWarnings("all")
    public static <K,V> List<ICacheInterceptor<K,V>> defaults() {
        List<ICacheInterceptor<K,V>> list = new ArrayList<>();
        list.add(new CacheInterceptorCommonCost());
        list.add(new CacheInterceptorEvict());
        list.add(new CacheInterceptorAof());
        list.add(new CacheInterceptorRefresh());
        return list;
    }

}
