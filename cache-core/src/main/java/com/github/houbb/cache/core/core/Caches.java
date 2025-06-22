package com.github.houbb.cache.core.core;

import com.github.houbb.cache.api.ICache;
import com.github.houbb.cache.core.core.impl.BasicCache;

/**
 * 核心实现
 * @since 1.0.0
 */
public class Caches {

    /**
     * 默认策略
     * @return 实现
     * @param <K> 泛型 key
     * @param <V> 泛型 value
     */
    public static <K,V> ICache<K,V> defaults() {
        return new BasicCache<>();
    }

}
