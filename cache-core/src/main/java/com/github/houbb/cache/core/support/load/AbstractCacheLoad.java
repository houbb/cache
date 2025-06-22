package com.github.houbb.cache.core.support.load;

import com.github.houbb.cache.api.ICacheLoad;
import com.github.houbb.cache.api.ICacheLoadContext;

/**
 * 抽象父类
 *
 * @param <K> 键
 * @param <V> 值
 * @since 1.0.0
 */
public abstract class AbstractCacheLoad<K,V> implements ICacheLoad<K,V> {

    protected ICacheLoadContext<K, V> context;

    public void init(ICacheLoadContext<K, V> context) {
        this.context = context;
    }

    public abstract void doLoad();

    @Override
    public void load() {
        doLoad();
    }

}
