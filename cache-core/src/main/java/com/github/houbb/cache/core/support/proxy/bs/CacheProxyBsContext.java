package com.github.houbb.cache.core.support.proxy.bs;

import com.github.houbb.cache.annotation.CacheInterceptor;
import com.github.houbb.cache.api.ICache;

import java.lang.reflect.Method;

/**
 * 代理引导类上下文
 * @author binbin.hou
 * @since 0.0.4
 */
public class CacheProxyBsContext implements ICacheProxyBsContext {

    /**
     * 目标
     * @since 0.0.4
     */
    private ICache target;

    /**
     * 入参
     * @since 0.0.4
     */
    private Object[] params;

    /**
     * 方法
     * @since 0.0.4
     */
    private Method method;

    /**
     * 拦截器
     * @since 0.0.5
     */
    private CacheInterceptor interceptor;

    /**
     * 新建对象
     * @return 对象
     * @since 0.0.4
     */
    public static CacheProxyBsContext newInstance(){
        return new CacheProxyBsContext();
    }

    @Override
    public ICache target() {
        return target;
    }

    @Override
    public CacheProxyBsContext target(ICache target) {
        this.target = target;
        return this;
    }

    @Override
    public Object[] params() {
        return params;
    }

    public CacheProxyBsContext params(Object[] params) {
        this.params = params;
        return this;
    }

    @Override
    public Method method() {
        return method;
    }

    @Override
    public Object process() throws Throwable {
        return this.method.invoke(target, params);
    }

    public CacheProxyBsContext method(Method method) {
        this.method = method;
        this.interceptor = method.getAnnotation(CacheInterceptor.class);
        return this;
    }

    @Override
    public CacheInterceptor interceptor() {
        return interceptor;
    }
}
