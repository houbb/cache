package com.github.houbb.cache.core.constant.enums;

/**
 * 拦截器类别
 * @since 1.0.0
 */
public enum CacheInterceptorType {
    COMMON("common", "通用"),
    REFRESH("refresh", "刷新"),
    AOF("aof", "AOF持久化操作"),
    EVICT("evict", "驱逐"),
    ;

    private final String code;
    private final String desc;

    CacheInterceptorType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String code() {
        return code;
    }

    public String desc() {
        return desc;
    }
}
