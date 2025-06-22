package com.github.houbb.cache.core.support.interceptor.evict;

import com.alibaba.fastjson.JSON;
import com.github.houbb.cache.api.ICacheContext;
import com.github.houbb.cache.api.ICacheEvict;
import com.github.houbb.cache.api.ICacheInterceptorContext;
import com.github.houbb.cache.core.constant.enums.CacheInterceptorType;
import com.github.houbb.cache.core.support.interceptor.AbstractCacheInterceptor;
import com.github.houbb.heaven.util.util.ArrayUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

/**
 * 驱除策略拦截器
 *
 * @author binbin.hou
 * @since 0.0.11
 */
public class CacheInterceptorEvict<K,V> extends AbstractCacheInterceptor<K, V> {

    private static final Log log = LogFactory.getLog(CacheInterceptorEvict.class);

    @Override
    protected String getType() {
        return CacheInterceptorType.EVICT.code();
    }

    @Override
    public void before(ICacheInterceptorContext<K,V> context) {
        final ICacheContext<K, V> cacheContext = context.cacheContext();
        final ICacheEvict<K,V> evict = cacheContext.evict();

        // 执行数据的淘汰
        Object[] params = context.params();
        K key = null;
        if(ArrayUtil.isNotEmpty(params)) {
            key = (K) params[0];
        }


        evict.evict(cacheContext, key);
    }

    @Override
    @SuppressWarnings("all")
    public void after(ICacheInterceptorContext<K,V> context) {
        final ICacheContext<K, V> cacheContext = context.cacheContext();

        ICacheEvict<K,V> evict = cacheContext.evict();

        String methodName = context.methodName();
        Object[] params = context.params();

        final K key = (K) params[0];
        if("remove".equals(methodName)) {
            evict.removeKey(cacheContext, key);
        } else {
            evict.updateKey(cacheContext, key);
        }
    }

}
