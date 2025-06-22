package com.github.houbb.cache.core.support.interceptor.refresh;

import com.github.houbb.cache.api.*;
import com.github.houbb.cache.core.constant.enums.CacheInterceptorType;
import com.github.houbb.cache.core.support.interceptor.AbstractCacheInterceptor;
import com.github.houbb.heaven.util.util.ArrayUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.util.Arrays;

/**
 * 刷新
 *
 * @author binbin.hou
 * @since 0.0.5
 */
public class CacheInterceptorRefresh<K,V> extends AbstractCacheInterceptor<K, V> {

    private static final Log log = LogFactory.getLog(CacheInterceptorRefresh.class);

    @Override
    protected String getType() {
        return CacheInterceptorType.REFRESH.code();
    }

    @Override
    public void before(ICacheInterceptorContext<K,V> context) {
        log.debug("[Cache] refresh start");
        final ICacheContext<K,V> cacheContext = context.cacheContext();

        // 刷新指定的 Key
        Object[] params = context.params();
        if(ArrayUtil.isNotEmpty(params)) {
            K key = (K) params[0];
            cacheContext.expire().refreshExpire(Arrays.asList(key));
            return;
        }

        // 刷新全部
        final ICacheMap<K,V> cacheMap = cacheContext.map();
        cacheContext.expire().refreshExpire(cacheMap.keySet());
    }

    @Override
    public void after(ICacheInterceptorContext<K,V> context) {
    }

}
