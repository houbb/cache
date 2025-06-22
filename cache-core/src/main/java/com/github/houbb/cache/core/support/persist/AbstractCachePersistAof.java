package com.github.houbb.cache.core.support.persist;

import com.github.houbb.cache.api.ICachePersistAof;

/**
 * 缓存持久化-适配器模式
 * @author binbin.hou
 * @since 1.0.0
 */
public abstract class AbstractCachePersistAof<K,V> extends AbstractCachePersist<K,V>
        implements ICachePersistAof<K,V> {

}
