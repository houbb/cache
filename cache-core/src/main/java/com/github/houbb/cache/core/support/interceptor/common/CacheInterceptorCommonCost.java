package com.github.houbb.cache.core.support.interceptor.common;

import com.github.houbb.cache.api.ICacheInterceptorContext;
import com.github.houbb.cache.core.constant.enums.CacheInterceptorType;
import com.github.houbb.cache.core.support.interceptor.AbstractCacheInterceptor;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.util.Arrays;

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
public class CacheInterceptorCommonCost<K,V> extends AbstractCacheInterceptor<K,V> {

    private static final Log log = LogFactory.getLog(CacheInterceptorCommonCost.class);

    @Override
    protected String getType() {
        return CacheInterceptorType.COMMON.code();
    }

    @Override
    public void before(ICacheInterceptorContext<K,V> context) {
        log.debug("[Cache] Cost start, methodName: {}， params={}", context.methodName(), Arrays.toString(context.params()));
    }

    @Override
    public void after(ICacheInterceptorContext<K,V> context) {
        long costMills = context.endMills()-context.startMills();
        final String methodName = context.methodName();
        log.debug("[Cache] Cost end, methodName: {}, cost: {}ms", methodName, costMills);
    }

}
