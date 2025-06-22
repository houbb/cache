package com.github.houbb.cache.core.model;

import com.github.houbb.cache.api.ICachePersistAofEntry;

import java.util.Arrays;

/**
 * AOF 持久化明细
 * @author binbin.hou
 * @since 0.0.10
 */
public class PersistAofEntry implements ICachePersistAofEntry {

    /**
     * 参数信息
     * @since 0.0.10
     */
    private Object[] params;

    /**
     * 方法名称
     * @since 0.0.10
     */
    private String methodName;

    /**
     * 新建对象实例
     * @return this
     * @since 0.0.10
     */
    public static PersistAofEntry newInstance() {
        return new PersistAofEntry();
    }

    @Override
    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return "PersistAofEntry{" +
                "params=" + Arrays.toString(params) +
                ", methodName='" + methodName + '\'' +
                '}';
    }

}
