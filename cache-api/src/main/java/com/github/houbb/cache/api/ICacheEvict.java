package com.github.houbb.cache.api;

/**
 * 驱除策略
 *
 * @author binbin.hou
 * @since 0.0.2
 */
public interface ICacheEvict<K, V> {

    /**
     * 驱除
     *
     * @param cacheContext 上下文
     * @param newKey 新添加的 Key
     * @since 0.0.2
     * @return 被移除的明细，没有时返回 null
     */
    ICacheEntry<K,V> evict(final ICacheContext<K,V> cacheContext, final K newKey);

    /**
     * 更新 key 信息
     *
     * @param cacheContext 上下文
     * @param evictKey     key
     * @since 0.0.11
     */
    void updateKey(final ICacheContext<K, V> cacheContext, final K evictKey);

    /**
     * 删除 key 信息
     *
     * @param cacheContext 上下文
     * @param evictKey     key
     * @since 0.0.11
     */
    void removeKey(final ICacheContext<K, V> cacheContext, final K evictKey);

}
