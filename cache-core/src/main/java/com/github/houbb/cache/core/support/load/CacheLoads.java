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

    /**
     * 文件 JSON
     * @param dbPath 文件路径
     * @param <K> key
     * @param <V> value
     * @return 值
     * @since 0.0.8
     */
    public static <K,V> ICacheLoad<K,V> dbJson(final String dbPath) {
        return new CacheLoadDbJson<>(dbPath);
    }

    /**
     * AOF 文件加载模式
     * @param dbPath 文件路径
     * @param <K> key
     * @param <V> value
     * @return 值
     * @since 0.0.10
     */
    public static <K,V> ICacheLoad<K,V> aof(final String dbPath) {
        return new CacheLoadAof<>(dbPath);
    }

}
