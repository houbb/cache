package com.github.houbb.cache.api;

import java.util.Map;

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
    Map<K, V> map();

    /**
     * 大小限制
     * @return 大小限制
     * @since 0.0.2
     */
    int size();

    /**
     * 驱除策略
     * @return 策略
     * @since 0.0.2
     */
    ICacheEvict<K,V> cacheEvict();

}
