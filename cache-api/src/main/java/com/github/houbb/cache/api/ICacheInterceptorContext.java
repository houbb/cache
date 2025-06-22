package com.github.houbb.cache.api;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 拦截器上下文接口
 *
 * （1）get
 * （2）put
 * （3）remove
 * （4）expire
 * （5）evict
 *
 * @author binbin.hou
 * @since 0.0.5
 * @param <K> key
 * @param <V> value
 */
public interface ICacheInterceptorContext<K,V> {

    /**
     * 类型列表
     * @return 结果
     * @since 1.0.0
     */
    List<String> typeList();

    /**
     * 缓存信息
     * @return 缓存信息
     * @since 0.0.5
     */
    ICacheContext<K, V> cacheContext();

    /**
     * 执行的方法信息
     * @return 方法
     * @since 0.0.5
     */
    String methodName();

    /**
     * 执行的参数
     * @return 参数
     * @since 0.0.5
     */
    Object[] params();

    /**
     * 开始时间
     * @return 时间
     * @since 0.0.5
     */
    long startMills();

    //-------------------------

    /**
     * 方法执行的结果
     * @return 结果
     * @since 0.0.5
     */
    Object result();

    /**
     * 结束时间
     * @return 时间
     * @since 0.0.5
     */
    long endMills();

}
