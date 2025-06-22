package com.github.houbb.cache.api;

import java.util.List;

/**
 * 缓存上下文
 * @author binbin.hou
 * @since 0.0.2
 */
public interface ICacheContext<K, V> {

    /**
     * map 信息
     * @return map
     * @since 0.0.2
     */
    ICacheMap<K, V> map();

    /**
     * 大小限制
     * @return 大小限制
     * @since 0.0.2
     */
    int size();

    /**
     * 驱除策略
     * v1.0.0 重命名为 evict
     * @return 策略
     * @since 0.0.2
     * @since 1.0.0 重命名为 evict
     */
    ICacheEvict<K,V> evict();

    /**
     * 过期策略
     * @return 策略
     * @since 1.0.0
     */
    ICacheExpire<K,V> expire();

    /**
     * 加载策略
     * @return 策略
     * @since 1.0.0
     */
    ICacheLoad<K,V> load();

    /**
     * 持久化策略
     * @return 策略
     * @since 1.0.0
     */
    ICachePersist<K,V> persist();

    /**
     * 操作监听类
     * @return 实现
     * @since 1.0.0
     */
    List<ICacheInterceptor<K,V>> interceptorList();

}
