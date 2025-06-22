package com.github.houbb.cache.api;

/**
 * 拦截器接口
 *
 * （1）耗时统计
 * （2）监听器
 *
 * @author binbin.hou
 * @since 0.0.5
 * @param <K> key
 * @param <V> value
 */
public interface ICacheInterceptor<K,V> extends ICacheOrder{

    /**
     * 是否满足触发条件
     * @param context 上下文
     * @return 结果
     * @since 1.0.0
     */
    boolean match(final ICacheInterceptorContext<K,V> context);

    /**
     * 方法执行之前
     * @param context 缓存上下文
     * @since 0.0.5
     */
    void before(ICacheInterceptorContext<K,V> context);

    /**
     * 方法执行之后
     * @param context 上下文
     * @since 0.0.5
     */
    void after(ICacheInterceptorContext<K,V> context);

    /**
     * 异常
     * @param context 上下文
     * @param exception 异常
     * @since 1.0.0
     */
    void exception(final ICacheInterceptorContext context, final Exception exception);

}
