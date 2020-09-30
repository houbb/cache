//package com.github.houbb.cache.core.support.expire;
//
//import com.github.houbb.cache.api.ICache;
//import com.github.houbb.cache.api.ICacheExpire;
//import com.github.houbb.heaven.util.util.CollectionUtil;
//import com.github.houbb.heaven.util.util.MapUtil;
//
//import java.util.*;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
///**
// * 缓存过期-时间排序策略
// *
// * 优点：删除时不用做过多消耗
// * 缺点：惰性删除不友好
// *
// * @author binbin.hou
// * @since 0.0.3
// * @param <K> key
// * @param <V> value
// */
//@Deprecated
//public class CacheExpireSort<K,V> implements ICacheExpire<K,V> {
//
//    /**
//     * 单次清空的数量限制
//     * @since 0.0.3
//     */
//    private static final int LIMIT = 100;
//
//    /**
//     * 排序缓存存储
//     *
//     * 使用按照时间排序的缓存处理。
//     * @since 0.0.3
//     */
//    private final Map<Long, List<K>> sortMap = new TreeMap<>(new Comparator<Long>() {
//        @Override
//        public int compare(Long o1, Long o2) {
//            return (int) (o1-o2);
//        }
//    });
//
//    /**
//     * 过期 map
//     *
//     * 空间换时间
//     * @since 0.0.3
//     */
//    private final Map<K, Long> expireMap = new HashMap<>();
//
//    /**
//     * 缓存实现
//     * @since 0.0.3
//     */
//    private final ICache<K,V> cache;
//
//    /**
//     * 线程执行类
//     * @since 0.0.3
//     */
//    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();
//
//    public CacheExpireSort(ICache<K, V> cache) {
//        this.cache = cache;
//        this.init();
//    }
//
//    /**
//     * 初始化任务
//     * @since 0.0.3
//     */
//    private void init() {
//        EXECUTOR_SERVICE.scheduleAtFixedRate(new ExpireThread(), 1, 1, TimeUnit.SECONDS);
//    }
//
//    /**
//     * 定时执行任务
//     * @since 0.0.3
//     */
//    private class ExpireThread implements Runnable {
//        @Override
//        public void run() {
//            //1.判断是否为空
//            if(MapUtil.isEmpty(sortMap)) {
//                return;
//            }
//
//            //2. 获取 key 进行处理
//            int count = 0;
//            for(Map.Entry<Long, List<K>> entry : sortMap.entrySet()) {
//                final Long expireAt = entry.getKey();
//                List<K> expireKeys = entry.getValue();
//
//                // 判断队列是否为空
//                if(CollectionUtil.isEmpty(expireKeys)) {
//                    sortMap.remove(expireAt);
//                    continue;
//                }
//                if(count >= LIMIT) {
//                    return;
//                }
//
//                // 删除的逻辑处理
//                long currentTime = System.currentTimeMillis();
//                if(currentTime >= expireAt) {
//                    Iterator<K> iterator = expireKeys.iterator();
//                    while (iterator.hasNext()) {
//                        K key = iterator.next();
//                        // 先移除本身
//                        iterator.remove();
//                        expireMap.remove(key);
//
//                        // 再移除缓存，后续可以通过惰性删除做补偿
//                        cache.remove(key);
//
//                        count++;
//                    }
//                } else {
//                    // 直接跳过，没有过期的信息
//                    return;
//                }
//            }
//        }
//    }
//
//    @Override
//    public void expire(K key, long expireAt) {
//        List<K> keys = sortMap.get(expireAt);
//        if(keys == null) {
//            keys = new ArrayList<>();
//        }
//        keys.add(key);
//
//        // 设置对应的信息
//        sortMap.put(expireAt, keys);
//        expireMap.put(key, expireAt);
//    }
//
//    @Override
//    public void refreshExpire(Collection<K> keyList) {
//        if(CollectionUtil.isEmpty(keyList)) {
//            return;
//        }
//
//        // 这样维护两套的代价太大，后续优化，暂时不用。
//    }
//
//    @Override
//    public Long expireTime(String key) {
//        th
//    }
//
//}
