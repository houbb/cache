package com.github.houbb.cache.api;

/**
 * 拦截器接口
 *
 * （1）耗时统计
 * （2）监听器
 *
 * @author binbin.hou
 * @since 0.0.6
 * @param <K> key
 * @param <V> value
 */
public interface ICacheRemoveListener<K,V> {

    /**
     * 监听
     * @param context 上下文
     * @since 0.0.6
     */
    void listen(final ICacheRemoveListenerContext<K,V> context);

}
