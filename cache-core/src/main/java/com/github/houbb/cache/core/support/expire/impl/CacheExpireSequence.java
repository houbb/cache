package com.github.houbb.cache.core.support.expire.impl;

import com.github.houbb.cache.core.support.expire.AbstractCacheExpire;
import com.github.houbb.heaven.util.util.MapUtil;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 缓存过期-普通策略
 *
 * 注意： 这个策略有一个问题，如果每一次都是顺序处理，但是 map 很大，会导致一直无法被处理到。
 * @author binbin.hou
 * @since 0.0.3
 * @param <K> key
 * @param <V> value
 */
@Deprecated
public class CacheExpireSequence<K,V> extends AbstractCacheExpire<K,V> {

    @Override
    protected void expireScheduleStart() {
        executorService.scheduleAtFixedRate(new ExpireThread(), 100, 100, TimeUnit.MILLISECONDS);
    }

    /**
     * 定时执行任务
     *
     * 说明：这个策略有一个问题，如果每一次都是顺序处理，但是 map 很大，会导致一直无法被处理到。
     *
     * @since 0.0.3
     */
    private class ExpireThread implements Runnable {
        final int limit = getLimitSize();

        @Override
        public void run() {
            //1.判断是否为空
            if(MapUtil.isEmpty(expireMap)) {
                return;
            }

            //2. 获取 key 进行处理
            int count = 0;
            for(Map.Entry<K, Long> entry : expireMap.entrySet()) {
                if(count >= limit) {
                    return;
                }

                removeExpireKey(entry.getKey(), entry.getValue());
                count++;
            }
        }
    }


}
