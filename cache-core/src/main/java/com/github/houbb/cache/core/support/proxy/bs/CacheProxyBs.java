package com.github.houbb.cache.core.support.proxy.bs;

import com.github.houbb.cache.annotation.Refresh;
import com.github.houbb.cache.api.ICache;

/**
 * 代理引导类
 * @author binbin.hou
 * @since 0.0.4
 */
public final class CacheProxyBs {

    private CacheProxyBs(){}

    /**
     * 代理上下文
     * @since 0.0.4
     */
    private ICacheProxyBsContext context;

    /**
     * 新建对象实例
     * @return 实例
     * @since 0.0.4
     */
    public static CacheProxyBs newInstance() {
        return new CacheProxyBs();
    }

    public CacheProxyBs context(ICacheProxyBsContext context) {
        this.context = context;
        return this;
    }

    /**
     * 执行
     * @return 结果
     * @since 0.0.4
     * @throws Throwable 异常
     */
    @SuppressWarnings("all")
    public Object execute() throws Throwable {
        // 基本信息
        final ICache cache = context.target();

        //1. 获取刷新注解信息
        Refresh refresh = context.refresh();
        if(refresh != null) {
            cache.cacheExpire().refreshExpire(cache.keySet());
        }

        //2. 正常执行
        return context.process();
    }

}
