package com.github.houbb.cache.core.support.interceptor;

import com.github.houbb.cache.api.ICacheInterceptor;
import com.github.houbb.cache.api.ICacheInterceptorContext;

public abstract class AbstractCacheInterceptor<K,V> implements ICacheInterceptor<K,V> {

    /**
     * 当前的类别
     * @return 结果
     */
    protected abstract String getType();

    @Override
    public boolean match(ICacheInterceptorContext<K, V> context) {
        String type = getType();
        return context.typeList().contains(type);
    }

    @Override
    public void before(ICacheInterceptorContext<K, V> context) {

    }

    @Override
    public void after(ICacheInterceptorContext<K, V> context) {

    }

    @Override
    public void exception(ICacheInterceptorContext context, Exception exception) {

    }

    @Override
    public int order() {
        return 0;
    }

}
