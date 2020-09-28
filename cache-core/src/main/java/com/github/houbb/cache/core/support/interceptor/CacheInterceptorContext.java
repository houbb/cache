package com.github.houbb.cache.core.support.interceptor;

import com.github.houbb.cache.api.ICache;
import com.github.houbb.cache.api.ICacheInterceptorContext;

import java.lang.reflect.Method;

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
public class CacheInterceptorContext<K,V> implements ICacheInterceptorContext<K,V> {

    private ICache<K,V> cache;

    /**
     * 执行的方法信息
     * @since 0.0.5
     */
    private Method method;

    /**
     * 执行的参数
     * @since 0.0.5
     */
    private Object[] params;

    /**
     * 方法执行的结果
     * @since 0.0.5
     */
    private Object result;

    /**
     * 开始时间
     * @since 0.0.5
     */
    private long startMills;

    /**
     * 结束时间
     * @since 0.0.5
     */
    private long endMills;

    public static <K,V> CacheInterceptorContext<K,V> newInstance() {
        return new CacheInterceptorContext<>();
    }

    @Override
    public ICache<K, V> cache() {
        return cache;
    }

    public CacheInterceptorContext<K, V> cache(ICache<K, V> cache) {
        this.cache = cache;
        return this;
    }

    @Override
    public Method method() {
        return method;
    }

    public CacheInterceptorContext<K, V> method(Method method) {
        this.method = method;
        return this;
    }

    @Override
    public Object[] params() {
        return params;
    }

    public CacheInterceptorContext<K, V> params(Object[] params) {
        this.params = params;
        return this;
    }

    @Override
    public Object result() {
        return result;
    }

    public CacheInterceptorContext<K, V> result(Object result) {
        this.result = result;
        return this;
    }

    @Override
    public long startMills() {
        return startMills;
    }

    public CacheInterceptorContext<K, V> startMills(long startMills) {
        this.startMills = startMills;
        return this;
    }

    @Override
    public long endMills() {
        return endMills;
    }

    public CacheInterceptorContext<K, V> endMills(long endMills) {
        this.endMills = endMills;
        return this;
    }
}
