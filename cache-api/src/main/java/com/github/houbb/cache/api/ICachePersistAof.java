package com.github.houbb.cache.api;

/**
 * 持久化缓存接口
 * @author binbin.hou
 * @since 1.0.0
 * @param <K> key
 * @param <V> value
 */
public interface ICachePersistAof<K, V> extends ICachePersist<K,V> {

    /**
     * 添加明细信息
     * @param aofEntry 明细
     * @return 结果
     * @since 1.0.0
     */
    boolean append(final ICachePersistAofEntry aofEntry);

}
