package com.github.houbb.cache.core.support.expire;

import com.github.houbb.cache.api.ICache;
import com.github.houbb.cache.api.ICacheExpire;
import com.github.houbb.cache.api.ICacheRemoveListener;
import com.github.houbb.cache.api.ICacheRemoveListenerContext;
import com.github.houbb.cache.core.constant.enums.CacheRemoveType;
import com.github.houbb.cache.core.exception.CacheRuntimeException;
import com.github.houbb.cache.core.support.evict.CacheEvictClock;
import com.github.houbb.cache.core.support.listener.remove.CacheRemoveListenerContext;
import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.heaven.util.util.MapUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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
public class CacheExpireRandom<K,V> implements ICacheExpire<K,V> {

    private static final Log log = LogFactory.getLog(CacheExpireRandom.class);

    /**
     * 单次清空的数量限制
     * @since 0.0.16
     */
    private static final int COUNT_LIMIT = 100;

    /**
     * 过期 map
     *
     * 空间换时间
     * @since 0.0.16
     */
    private final Map<K, Long> expireMap = new HashMap<>();

    /**
     * 缓存实现
     * @since 0.0.16
     */
    private final ICache<K,V> cache;

    /**
     * 是否启用快模式
     * @since 0.0.16
     */
    private volatile boolean fastMode = false;

    /**
     * 线程执行类
     * @since 0.0.16
     */
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    public CacheExpireRandom(ICache<K, V> cache) {
        this.cache = cache;
        this.init();
    }

    /**
     * 初始化任务
     * @since 0.0.16
     */
    private void init() {
        EXECUTOR_SERVICE.scheduleAtFixedRate(new ExpireThreadRandom(), 10, 10, TimeUnit.SECONDS);
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

        //2. 获取 key 进行处理
        int count = 0;
        while (true) {
            //2.1 返回判断
            if(count >= COUNT_LIMIT) {
                log.info("过期淘汰次数已经达到最大次数: {}，完成本次执行。", COUNT_LIMIT);
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
            boolean expireFlag = expireKey(key, expireAt);
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

    @Override
    public void expire(K key, long expireAt) {
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
                expireKey(key, expireAt);
            }
        } else {
            for(Map.Entry<K, Long> entry : expireMap.entrySet()) {
                this.expireKey(entry.getKey(), entry.getValue());
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
    private boolean expireKey(final K key, final Long expireAt) {
        if(expireAt == null) {
            return false;
        }

        long currentTime = System.currentTimeMillis();
        if(currentTime >= expireAt) {
            expireMap.remove(key);
            // 再移除缓存，后续可以通过惰性删除做补偿
            V removeValue = cache.remove(key);

            // 执行淘汰监听器
            ICacheRemoveListenerContext<K,V> removeListenerContext = CacheRemoveListenerContext.<K,V>newInstance().key(key).value(removeValue).type(CacheRemoveType.EXPIRE.code());
            for(ICacheRemoveListener<K,V> listener : cache.removeListeners()) {
                listener.listen(removeListenerContext);
            }

            return true;
        }

        return false;
    }

}
