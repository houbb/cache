package com.github.houbb.cache.core.support.expire.impl;

import com.github.houbb.cache.core.support.expire.AbstractCacheExpire;
import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.heaven.util.util.MapUtil;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 缓存过期-时间排序策略
 *
 * 优点：定时删除时不用做过多消耗
 * 缺点：惰性删除不友好
 *
 * @author binbin.hou
 * @since 0.0.3
 * @param <K> key
 * @param <V> value
 */
public class CacheExpireSort<K,V> extends AbstractCacheExpire<K,V> {

    /**
     * 排序缓存存储
     *
     * 使用按照时间排序的缓存处理。
     * @since 0.0.3
     */
    private final Map<Long, List<K>> sortMap = new TreeMap<>(new Comparator<Long>() {
        @Override
        public int compare(Long o1, Long o2) {
            return (int) (o1-o2);
        }
    });

    @Override
    protected void expireScheduleStart() {
        executorService.scheduleAtFixedRate(new ExpireThread(), 100, 100, TimeUnit.MILLISECONDS);
    }

    /**
     * 定时执行任务
     * @since 0.0.3
     */
    private class ExpireThread implements Runnable {
        final int limit = getLimitSize();

        @Override
        public void run() {
            //1.判断是否为空
            if(MapUtil.isEmpty(sortMap)) {
                return;
            }

            //2. 获取 key 进行处理
            int count = 0;
            for(Map.Entry<Long, List<K>> entry : sortMap.entrySet()) {
                final Long expireAt = entry.getKey();
                List<K> expireKeys = entry.getValue();

                // 判断队列是否为空
                if(CollectionUtil.isEmpty(expireKeys)) {
                    sortMap.remove(expireAt);
                    continue;
                }
                if(count >= limit) {
                    return;
                }

                // 删除的逻辑处理
                long currentTime = System.currentTimeMillis();
                if(currentTime >= expireAt) {
                    Iterator<K> iterator = expireKeys.iterator();
                    while (iterator.hasNext()) {
                        K key = iterator.next();
                        // 先移除本身
                        iterator.remove();

                        // 删除父类
                        removeExpireKey(key, currentTime);

                        count++;
                    }
                } else {
                    // 直接跳过，没有过期的信息
                    return;
                }
            }
        }
    }


    @Override
    public void expireAt(K key, long expireAt) {
        List<K> keys = sortMap.get(expireAt);
        if(keys == null) {
            keys = new ArrayList<>();
        }
        keys.add(key);

        // 设置对应的信息
        sortMap.put(expireAt, keys);
        expireMap.put(key, expireAt);
    }

    @Override
    public void refreshExpire(Collection<K> keyList) {
        if(CollectionUtil.isEmpty(keyList)) {
            return;
        }

        // 这样维护两套的代价太大，后续优化，暂时不用。
        // 判断大小，小的作为外循环
        final int expireSize = expireMap.size();
        if(expireSize <= keyList.size()) {
            // 一般过期的数量都是较少的
            for(Map.Entry<K,Long> entry : expireMap.entrySet()) {
                K key = entry.getKey();

                // 这里直接执行过期处理，不再判断是否存在于集合中。
                // 因为基于集合的判断，时间复杂度为 O(n)
                this.removeExpireKey(key);
            }
        } else {
            for(K key : keyList) {
                this.removeExpireKey(key);
            }
        }
    }

    /**
     * 移除过期信息
     * @param key key
     * @since 0.0.10
     */
    private void removeExpireKey(final K key) {
        Long expireTime = expireMap.get(key);
        if(expireTime != null) {
            final long currentTime = System.currentTimeMillis();
            if(currentTime >= expireTime) {
                // 删除过期的 key
                super.removeExpireKey(key, currentTime);

                List<K> expireKeys = sortMap.get(expireTime);
                expireKeys.remove(key);
                sortMap.put(expireTime, expireKeys);
            }
        }
    }

}
