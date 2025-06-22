package com.github.houbb.cache.core.support.expire;

import com.github.houbb.cache.api.ICacheExpire;
import com.github.houbb.cache.core.support.expire.impl.CacheExpireNone;
import com.github.houbb.cache.core.support.expire.impl.CacheExpireRandom;
import com.github.houbb.cache.core.support.expire.impl.CacheExpireSequence;

/**
 * 缓存淘汰策略工具类
 *
 * @since 1.0.0
 */
public class CacheExpires {

    /**
     * 默认策略
     * @return 实现
     * @param <K> 泛型 key
     * @param <V> 泛型 value
     */
    public static <K,V> ICacheExpire<K,V> defaults() {
        return random();
    }

    /**
     * 排序策略
     *
     * 缺点：内存占用
     * @return 实现
     * @param <K> 泛型 key
     * @param <V> 泛型 value
     */
    public static <K,V> ICacheExpire<K,V> sort() {
        return new CacheExpireRandom<>();
    }

    /**
     * 随机策略
     * @return 实现
     * @param <K> 泛型 key
     * @param <V> 泛型 value
     */
    public static <K,V> ICacheExpire<K,V> random() {
        return new CacheExpireRandom<>();
    }

    /**
     * 空策略
     * @return 实现
     * @param <K> 泛型 key
     * @param <V> 泛型 value
     * @since 1.0.0
     */
    public static <K,V> ICacheExpire<K,V> sequence() {
        return new CacheExpireSequence<>();
    }

    /**
     * 空策略
     * @return 实现
     * @param <K> 泛型 key
     * @param <V> 泛型 value
     */
    public static <K,V> ICacheExpire<K,V> none() {
        return new CacheExpireNone<>();
    }

}
