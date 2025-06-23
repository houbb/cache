package com.github.houbb.cache.core.support.interceptor.evict;

import com.github.houbb.cache.api.ICacheContext;
import com.github.houbb.cache.api.ICacheEvict;
import com.github.houbb.cache.api.ICacheInterceptorContext;
import com.github.houbb.cache.core.constant.enums.CacheInterceptorType;
import com.github.houbb.cache.core.support.interceptor.AbstractCacheInterceptor;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

/**
 * 驱除删除策略拦截器
 *
 * @author binbin.hou
 * @since 1.0.1
 */
public class CacheInterceptorEvictRemove<K,V> extends AbstractCacheInterceptor<K, V> {

    private static final Log log = LogFactory.getLog(CacheInterceptorEvictRemove.class);

    @Override
    protected String getType() {
        return CacheInterceptorType.EVICT_REMOVE.code();
    }

    @Override
    public void before(ICacheInterceptorContext<K, V> context) {
    }

    @Override
    @SuppressWarnings("all")
    public void after(ICacheInterceptorContext<K, V> context) {
        final ICacheContext<K, V> cacheContext = context.cacheContext();

        ICacheEvict<K, V> evict = cacheContext.evict();

        String methodName = context.methodName();
        Object[] params = context.params();

        final K key = (K) params[0];
        evict.removeKey(cacheContext, key);
    }

}
