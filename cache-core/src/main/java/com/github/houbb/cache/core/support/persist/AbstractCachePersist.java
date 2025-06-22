package com.github.houbb.cache.core.support.persist;

import com.github.houbb.cache.api.ICachePersist;
import com.github.houbb.cache.api.ICachePersistContext;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 缓存持久化-适配器模式
 * @author binbin.hou
 * @since 0.0.10
 */
public abstract class AbstractCachePersist<K,V> implements ICachePersist<K,V> {

    private static final Log log = LogFactory.getLog(AbstractCachePersist.class);

    protected ICachePersistContext<K,V> persistContext;

    /**
     * 调度线程池
     *
     * @since 1.0.0
     */
    protected ScheduledExecutorService executorService;

    /**
     * 持久化实现
     * @since 1.0.0
     */
    protected abstract void doPersist();

    protected void initExecutorService() {
        this.executorService = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * 开始持久化调度
     */
    protected void persistScheduleStart() {
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    log.info("[Cache] 开始持久化缓存信息");
                    doPersist();
                    log.info("[Cache] 完成持久化缓存信息");
                } catch (Exception exception) {
                    log.error("[Cache] 文件持久化异常", exception);
                }
            }
        }, delay(), period(), timeUnit());
    }

    @Override
    public ICachePersist<K, V> init(ICachePersistContext<K, V> persistContext) {
        this.persistContext = persistContext;

        initExecutorService();
        this.persistScheduleStart();

        return this;
    }

    protected long delay() {
        return 1;
    }

    protected long period() {
        return 1;
    }

    protected TimeUnit timeUnit() {
        return TimeUnit.SECONDS;
    }

}
