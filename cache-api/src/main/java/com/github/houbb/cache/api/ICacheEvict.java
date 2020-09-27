package com.github.houbb.cache.api;

/**
 * 驱除策略
 *
 * @author binbin.hou
 * @since 0.0.2
 */
public interface ICacheEvict<K, V> {

    /**
     * 驱除策略
     *
     * @param context 上下文
     * @since 0.0.2
     */
    void evict(final ICacheEvictContext<K, V> context);

}
