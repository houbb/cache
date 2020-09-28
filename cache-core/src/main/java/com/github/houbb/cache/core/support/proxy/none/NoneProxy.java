/*
 * Copyright (c)  2019. houbinbin Inc.
 * async All rights reserved.
 */

package com.github.houbb.cache.core.support.proxy.none;

import com.github.houbb.cache.core.support.proxy.ICacheProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * <p> 没有代理 </p>
 *
 * <pre> Created: 2019/3/5 10:23 PM  </pre>
 * <pre> Project: cache  </pre>
 *
 * @author houbinbin
 * @since 0.0.4
 */
public class NoneProxy implements InvocationHandler, ICacheProxy {

    /**
     * 代理对象
     */
    private final Object target;

    public NoneProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(proxy, args);
    }

    /**
     * 返回原始对象，没有代理
     * @return 原始对象
     */
    @Override
    public Object proxy() {
        return this.target;
    }

}
