package com.github.houbb.cache.core.support.expire.impl;

import com.github.houbb.cache.core.exception.CacheRuntimeException;
import com.github.houbb.cache.core.support.expire.AbstractCacheExpire;
import com.github.houbb.heaven.util.util.MapUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 缓存过期-普通策略随机
 *
 * @author binbin.hou
 * @since 0.0.16
 * @param <K> key
 * @param <V> value
 */
public class CacheExpireRandom<K,V> extends AbstractCacheExpire<K,V> {

    private static final Log log = LogFactory.getLog(CacheExpireRandom.class);

    /**
     * 是否启用快模式
     * @since 0.0.16
     */
    private volatile boolean fastMode = false;

    public CacheExpireRandom(boolean fastMode) {
        this.fastMode = fastMode;
    }

    public CacheExpireRandom() {
        this(false);
    }

    @Override
    protected void expireScheduleStart() {
        executorService.scheduleAtFixedRate(new ExpireThreadRandom(), 10, 10, TimeUnit.SECONDS);
    }

    /**
     * 定时执行任务
     * @since 0.0.16
     */
    private class ExpireThreadRandom implements Runnable {
        @Override
        public void run() {
            //1.判断是否为空
            if(MapUtil.isEmpty(expireMap)) {
                log.info("expireMap 信息为空，直接跳过本次处理。");
                return;
            }

            //2. 是否启用快模式
            if(fastMode) {
                expireKeys(10L);
            }

            //3. 缓慢模式
            expireKeys(100L);
        }
    }


    /**
     * 过期信息
     * @param timeoutMills 超时时间
     * @since 0.0.16
     */
    private void expireKeys(final long timeoutMills) {
        // 设置超时时间 100ms
        final long timeLimit = System.currentTimeMillis() + timeoutMills;
        // 恢复 fastMode
        this.fastMode = false;

        final int countLimit = getLimitSize();

        //2. 获取 key 进行处理
        int count = 0;
        while (true) {
            //2.1 返回判断
            if(count >= countLimit) {
                log.info("过期淘汰次数已经达到最大次数: {}，完成本次执行。", countLimit);
                return;
            }
            if(System.currentTimeMillis() >= timeLimit) {
                this.fastMode = true;
                log.info("过期淘汰已经达到限制时间，中断本次执行，设置 fastMode=true;");
                return;
            }

            //2.2 随机过期
            K key = getRandomKey();
            Long expireAt = expireMap.get(key);
            boolean expireFlag = removeExpireKey(key, expireAt);
            log.debug("key: {} 过期执行结果 {}", key, expireFlag);

            //2.3 信息更新
            count++;
        }
    }


    /**
     * 随机获取一个 key 信息
     * @return 随机返回的 keys
     * @since 0.0.16
     */
    private K getRandomKey() {
        Random random = ThreadLocalRandom.current();

        Set<K> keySet = expireMap.keySet();
        List<K> list = new ArrayList<>(keySet);
        int randomIndex = random.nextInt(list.size());
        return list.get(randomIndex);
    }

    /**
     * 随机获取一个 key 信息
     * @return 随机返回的 keys
     * @since 0.0.16
     */
    private K getRandomKey2() {
        Random random = ThreadLocalRandom.current();
        int randomIndex = random.nextInt(expireMap.size());

        // 遍历 keys
        Iterator<K> iterator = expireMap.keySet().iterator();
        int count = 0;
        while (iterator.hasNext()) {
            K key = iterator.next();

            if(count == randomIndex) {
                return key;
            }
            count++;
        }

        // 正常逻辑不会到这里
        throw new CacheRuntimeException("对应信息不存在");
    }

    /**
     * 批量获取多个 key 信息
     * @param sizeLimit 大小限制
     * @return 随机返回的 keys
     * @since 0.0.16
     */
    private Set<K> getRandomKeyBatch(final int sizeLimit) {
        Random random = ThreadLocalRandom.current();
        int randomIndex = random.nextInt(expireMap.size());

        // 遍历 keys
        Iterator<K> iterator = expireMap.keySet().iterator();
        int count = 0;

        Set<K> keySet = new HashSet<>();
        while (iterator.hasNext()) {
            // 判断列表大小
            if(keySet.size() >= sizeLimit) {
                return keySet;
            }

            K key = iterator.next();
            // index 向后的位置，全部放进来。
            if(count >= randomIndex) {
                keySet.add(key);
            }
            count++;
        }

        // 正常逻辑不会到这里
        throw new CacheRuntimeException("对应信息不存在");
    }

}
