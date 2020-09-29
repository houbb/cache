package com.github.houbb.cache.core.support.load;

import com.github.houbb.cache.api.ICache;
import com.github.houbb.cache.api.ICacheLoad;

/**
 * 加载策略-无
 * @author binbin.hou
 * @since 0.0.7
 */
public class CacheLoadNone<K,V> implements ICacheLoad<K,V> {

    @Override
    public void load(ICache<K, V> cache) {
        //nothing...
    }

}
