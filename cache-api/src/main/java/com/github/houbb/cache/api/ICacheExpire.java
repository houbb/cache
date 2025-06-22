package com.github.houbb.cache.api;

import java.util.Collection;

/**
 * 缓存过期接口
 * @author binbin.hou
 * @since 0.0.3
 */
public interface ICacheExpire<K,V> {

    /**
     * 初始化
     * @param cacheExpireContext 上下文
     * @return 结果
     */
    ICacheExpire<K, V> init(final ICacheExpireContext<K, V> cacheExpireContext);

    /**
     * 指定过期信息
     * @param key key
     * @param expireAt 什么时候过期
     * @since 0.0.3
     */
    void expireAt(final K key, final long expireAt);

    /**
     * 惰性删除中需要处理的 keys
     * @param keyList keys
     * @since 0.0.3
     */
    void refreshExpire(final Collection<K> keyList);

    /**
     * 待过期的 key
     * 不存在，则返回 null
     *
     * @param key 待过期的 key
     * @return 结果
     * @since 0.0.8
     */
    Long expireTime(final K key);

}
