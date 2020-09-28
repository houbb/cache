package com.github.houbb.cache.core.support.interceptor.common;

import com.github.houbb.cache.api.ICacheInterceptor;
import com.github.houbb.cache.api.ICacheInterceptorContext;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

/**
 * 耗时统计
 *
 * （1）耗时
 * （2）慢日志
 * @author binbin.hou
 * @since 0.0.5
 * @param <K> key
 * @param <V> value
 */
public class CacheInterceptorCost<K,V> implements ICacheInterceptor<K,V> {

    private static final Log log = LogFactory.getLog(CacheInterceptorCost.class);

    @Override
    public void before(ICacheInterceptorContext<K,V> context) {
        log.debug("Cost start, method: {}", context.method().getName());
    }

    @Override
    public void after(ICacheInterceptorContext<K,V> context) {
        long cost = context.endMills()-context.startMills();
        log.debug("Cost end, method: {}, cost: {}ms", context.method().getName(), cost);
    }

}
