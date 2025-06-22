package com.github.houbb.cache.core.core.impl;

import com.github.houbb.cache.api.ICache;
import com.github.houbb.cache.api.ICacheContext;
import com.github.houbb.cache.api.ICacheInterceptor;
import com.github.houbb.cache.core.constant.enums.CacheInterceptorType;
import com.github.houbb.cache.core.support.interceptor.CacheInterceptorContext;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class BasicCache<K,V> implements ICache<K,V> {

    protected ICacheContext<K, V> cacheContext;

    @Override
    public ICache<K, V> init(ICacheContext<K, V> context) {
        this.cacheContext = context;
        return this;
    }


    protected CacheInterceptorContext<K,V> doFilterBefore(final String methodName,
                                                          final List<String> typeList,
                                                          Object...params) {
        // 正序循环
        CacheInterceptorContext<K,V> interceptorContext = new CacheInterceptorContext<>();
        interceptorContext.methodName(methodName);
        interceptorContext.params(params);
        interceptorContext.cacheContext(cacheContext);
        interceptorContext.startMills(System.currentTimeMillis());
        interceptorContext.typeList(typeList);

        // 列表
        final List<ICacheInterceptor<K,V>> cacheInterceptorList = cacheContext.interceptorList();
        for(ICacheInterceptor<K,V> interceptor : cacheInterceptorList) {
            if(interceptor.match(interceptorContext)) {
                interceptor.before(interceptorContext);
            }
        }
        return interceptorContext;
    }


    protected void doFilterAfter(final CacheInterceptorContext<K,V> interceptorContext,
                                 final Object result) {
        interceptorContext.endMills(System.currentTimeMillis());
        interceptorContext.result(result);

        // 倒序循环
        final List<ICacheInterceptor<K,V>> cacheInterceptorList = cacheContext.interceptorList();
        for(int i = cacheInterceptorList.size()-1; i >=0 ; i--) {
            ICacheInterceptor<K,V> interceptor = cacheInterceptorList.get(i);
            if(interceptor.match(interceptorContext)) {
                interceptor.after(interceptorContext);
            }
        }
    }

    @Override
    public ICache<K, V> expireAt(K key, long linuxTime) {
        CacheInterceptorContext<K,V> context = doFilterBefore("expireAt",
                Arrays.asList(CacheInterceptorType.COMMON.code(), CacheInterceptorType.AOF.code()),
                key, linuxTime);

        cacheContext.expire().expireAt(key, linuxTime);

        doFilterAfter(context, null);

        return this;
    }

    @Override
    public int size() {
        CacheInterceptorContext<K,V> context = doFilterBefore("size",
                Arrays.asList(CacheInterceptorType.COMMON.code(), CacheInterceptorType.REFRESH.code()));

        int result = cacheContext.map().size();

        doFilterAfter(context, result);

        return result;
    }

    @Override
    public boolean isEmpty() {
        CacheInterceptorContext<K,V> context = doFilterBefore("isEmpty",
                Arrays.asList(CacheInterceptorType.COMMON.code(), CacheInterceptorType.REFRESH.code()));

        boolean result = cacheContext.map().isEmpty();

        doFilterAfter(context, result);

        return result;
    }

    @Override
    public boolean containsKey(K key) {
        CacheInterceptorContext<K,V> context = doFilterBefore("containsKey",
                Arrays.asList(CacheInterceptorType.COMMON.code(), CacheInterceptorType.REFRESH.code()),
                key);

        boolean result = cacheContext.map().containsKey(key);

        doFilterAfter(context, result);

        return result;
    }

    @Override
    public V get(K key) {
        CacheInterceptorContext<K,V> context = doFilterBefore("get",
                Arrays.asList(CacheInterceptorType.COMMON.code(), CacheInterceptorType.REFRESH.code()),
                key);

        V result = cacheContext.map().get(key);

        doFilterAfter(context, result);

        return result;
    }

    @Override
    public V put(K key, V value) {
        CacheInterceptorContext<K,V> context = doFilterBefore("put",
                Arrays.asList(CacheInterceptorType.COMMON.code(),
                        CacheInterceptorType.REFRESH.code(),
                        CacheInterceptorType.EVICT.code(),
                        CacheInterceptorType.AOF.code()
                        ),
                key, value);

        V result = cacheContext.map().put(key,value);

        doFilterAfter(context, result);

        return result;
    }

    @Override
    public V remove(K key) {
        CacheInterceptorContext<K,V> context = doFilterBefore("remove",
                Arrays.asList(CacheInterceptorType.COMMON.code(),
                        CacheInterceptorType.REFRESH.code(),
                        CacheInterceptorType.EVICT.code(),
                        CacheInterceptorType.AOF.code()
                ),
                key);

        V result = cacheContext.map().remove(key);

        doFilterAfter(context, result);

        return result;
    }

    @Override
    public Set<K> keySet() {
        CacheInterceptorContext<K,V> context = doFilterBefore("keySet",
                Arrays.asList(CacheInterceptorType.COMMON.code(),
                        CacheInterceptorType.REFRESH.code()
                ));

        Set<K> result = cacheContext.map().keySet();

        doFilterAfter(context, result);

        return result;
    }

}
