package com.github.houbb.cache.core.support.interceptor.aof;

import com.github.houbb.cache.api.ICacheInterceptorContext;
import com.github.houbb.cache.api.ICachePersist;
import com.github.houbb.cache.core.constant.enums.CacheInterceptorType;
import com.github.houbb.cache.core.model.PersistAofEntry;
import com.github.houbb.cache.core.support.interceptor.AbstractCacheInterceptor;
import com.github.houbb.cache.core.support.persist.AbstractCachePersistAof;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

/**
 * 顺序追加模式
 *
 * AOF 持久化到文件，暂时不考虑 buffer 等特性。
 * @author binbin.hou
 * @since 0.0.10
 */
public class CacheInterceptorAof<K,V> extends AbstractCacheInterceptor<K, V> {

    private static final Log log = LogFactory.getLog(CacheInterceptorAof.class);

    @Override
    protected String getType() {
        return CacheInterceptorType.AOF.code();
    }

    @Override
    public void before(ICacheInterceptorContext<K,V> context) {
    }

    @Override
    public void after(ICacheInterceptorContext<K,V> context) {
        // 持久化类
        ICachePersist<K,V> persist = context.cacheContext().persist();

        final String methodName = context.methodName();
        final Object[] params = context.params();

        // 这里要求必须是 aof 的子类，是否不太好？
        if(persist instanceof AbstractCachePersistAof) {
            AbstractCachePersistAof<K,V> cachePersistAof = (AbstractCachePersistAof<K,V>) persist;

            PersistAofEntry aofEntry = PersistAofEntry.newInstance();
            aofEntry.setMethodName(methodName);
            aofEntry.setParams(params);

            // 直接持久化
            log.debug("[Cache] AOF 开始追加文件内容：{}", aofEntry);
            cachePersistAof.append(aofEntry);
            log.debug("[Cache] AOF 完成追加文件内容：{}", aofEntry);
        }
    }

}
