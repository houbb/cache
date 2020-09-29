package com.github.houbb.cache.core.support.load;

import com.github.houbb.cache.api.ICacheLoad;

/**
 *
 * 加载策略工具类
 * @author binbin.hou
 * @since 0.0.7
 */
public final class CacheLoads {

    private CacheLoads(){}

    /**
     * 无加载
     * @param <K> key
     * @param <V> value
     * @return 值
     * @since 0.0.7
     */
    public static <K,V> ICacheLoad<K,V> none() {
        return new CacheLoadNone<>();
    }


}
