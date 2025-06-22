package com.github.houbb.cache.api;

/**
 * AOF 持久化明细
 * @author binbin.hou
 * @since 1.0.0
 */
public interface ICachePersistAofEntry {

    /**
     * 参数信息
     * @since 1.0.0
     */
    Object[] getParams();

    /**
     * 方法名称
     * @since 1.0.0
     */
    String getMethodName();

}
