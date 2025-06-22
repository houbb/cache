package com.github.houbb.cache.core.support.interceptor;

import com.github.houbb.cache.api.ICacheContext;
import com.github.houbb.cache.api.ICacheInterceptorContext;

import java.util.List;

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

    /**
     * 类型列表
     * @since 1.0.0
     */
    private List<String> typeList;

    /**
     * 缓存上下文
     * @since 1.0.0
     */
    private ICacheContext<K, V> cacheContext;

    /**
     * 执行的方法信息
     * @since 0.0.5
     */
    private String methodName;

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
    public List<String> typeList() {
        return typeList;
    }

    public CacheInterceptorContext<K, V> typeList(List<String> typeList) {
        this.typeList = typeList;
        return this;
    }

    @Override
    public ICacheContext<K, V> cacheContext() {
        return cacheContext;
    }

    public CacheInterceptorContext<K, V> cacheContext(ICacheContext<K, V> cacheContext) {
        this.cacheContext = cacheContext;
        return this;
    }

    @Override
    public String methodName() {
        return methodName;
    }

    public CacheInterceptorContext<K, V> methodName(String methodName) {
        this.methodName = methodName;
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
