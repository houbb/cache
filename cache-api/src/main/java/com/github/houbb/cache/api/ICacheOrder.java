package com.github.houbb.cache.api;

/**
 * 指定顺序，用于排序时使用
 *
 * 1. 顺序越小，优先级越高
 *
 * @since 1.0.0
 */
public interface ICacheOrder {

    /**
     * 对应的顺序
     * @return 顺序
     * @since 1.0.0
     */
    int order();

}
