package com.github.houbb.cache.api;

/**
 * 慢日志监听器上下文
 *
 * @author binbin.hou
 * @since 0.0.9
 */
public interface ICacheSlowListenerContext {

    /**
     * 方法名称
     * @return 方法名称
     * @since 0.0.9
     */
    String methodName();

    /**
     * 参数信息
     * @return 参数列表
     * @since 0.0.9
     */
    Object[] params();

    /**
     * 方法结果
     * @return 方法结果
     * @since 0.0.9
     */
    Object result();

    /**
     * 开始时间
     * @return 时间
     * @since 0.0.9
     */
    long startTimeMills();

    /**
     * 结束时间
     * @return 结束时间
     * @since 0.0.9
     */
    long endTimeMills();

    /**
     * 消耗时间
     * @return 耗时
     * @since 0.0.9
     */
    long costTimeMills();

}
