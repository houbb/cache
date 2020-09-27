package com.github.houbb.cache.api;

import java.util.Collection;

/**
 * 缓存过期接口
 * @author binbin.hou
 * @since 0.0.3
 */
public interface ICacheExpire<K,V> {

    /**
     * 指定过期信息
     * @param key key
     * @param expireAt 什么时候过期
     * @since 0.0.3
     */
    void expire(final K key, final long expireAt);

    /**
     * 惰性删除中需要处理的 keys
     * @param keyList keys
     * @since 0.0.3
     */
    void refreshExpire(final Collection<K> keyList);

}
