package com.github.houbb.cache.core.support.expire;

import com.github.houbb.cache.api.ICacheExpire;
import com.github.houbb.cache.api.ICacheExpireContext;
import com.github.houbb.heaven.util.common.ArgUtil;
import com.github.houbb.heaven.util.util.CollectionUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 缓存过期-统一父类策略
 *
 * @since 1.0.0
 */
public abstract class AbstractCacheExpire<K,V> implements ICacheExpire<K,V> {

    /**
     * 上下文
     */
    protected ICacheExpireContext<K,V> cacheExpireContext;

    /**
     * 过期 map
     *
     * 空间换时间
     * @since 0.0.3
     */
    protected final Map<K, Long> expireMap = new HashMap<>();

    /**
     * 调度线程池
     *
     * @since 1.0.0
     */
    protected ScheduledExecutorService executorService;

    protected void initExecutorService() {
        this.executorService = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * 开始过期调度
     */
    protected abstract void expireScheduleStart();

    /**
     * 每一次批量处理的数量限制
     * @return 结果
     * @since 1.0.0
     */
    protected int getLimitSize() {
        return 1000;
    }

    @Override
    public ICacheExpire<K, V> init(ICacheExpireContext<K, V> cacheExpireContext) {
        ArgUtil.notNull(cacheExpireContext, "cacheExpireContext");

        this.cacheExpireContext = cacheExpireContext;

        // 初始化线程池
        this.initExecutorService();

        // 定时调度
        this.expireScheduleStart();

        return this;
    }

    @Override
    public void expireAt(K key, long expireAt) {
        expireMap.put(key, expireAt);
    }

    @Override
    public void refreshExpire(Collection<K> keyList) {
        if(CollectionUtil.isEmpty(keyList)) {
            return;
        }

        // 判断大小，小的作为外循环。一般都是过期的 keys 比较小。
        if(keyList.size() <= expireMap.size()) {
            for(K key : keyList) {
                Long expireAt = expireMap.get(key);
                removeExpireKey(key, expireAt);
            }
        } else {
            for(Map.Entry<K, Long> entry : expireMap.entrySet()) {
                this.removeExpireKey(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public Long expireTime(K key) {
        return expireMap.get(key);
    }


    /**
     * 过期处理 key
     * @param key key
     * @param expireAt 过期时间
     * @since 0.0.16
     * @return 是否执行过期
     */
    protected boolean removeExpireKey(final K key, final Long expireAt) {
        if(expireAt == null) {
            return false;
        }

        long currentTime = System.currentTimeMillis();
        if(currentTime >= expireAt) {
            expireMap.remove(key);
            // 再移除缓存，后续可以通过惰性删除做补偿
            V removeValue = cacheExpireContext.map().remove(key);

            return true;
        }

        return false;
    }

}
