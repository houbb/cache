package com.github.houbb.cache.core.support.evict;

import com.github.houbb.cache.api.ICache;
import com.github.houbb.cache.api.ICacheEntry;
import com.github.houbb.cache.api.ICacheEvictContext;
import com.github.houbb.cache.core.exception.CacheRuntimeException;
import com.github.houbb.cache.core.model.CacheEntry;
import com.github.houbb.cache.core.model.FreqNode;
import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.util.*;

/**
 * 丢弃策略-LFU 最少使用频次
 * @author binbin.hou
 * @since 0.0.14
 */
public class CacheEvictLfu<K,V> extends AbstractCacheEvict<K,V> {

    private static final Log log = LogFactory.getLog(CacheEvictLfu.class);

    /**
     * key 映射信息
     * @since 0.0.14
     */
    private final Map<K, FreqNode<K,V>> keyMap;

    /**
     * 频率 map
     * @since 0.0.14
     */
    private final Map<Integer, LinkedHashSet<FreqNode<K,V>>> freqMap;

    /**
     *
     * 最小频率
     * @since 0.0.14
     */
    private int minFreq;

    public CacheEvictLfu() {
        this.keyMap = new HashMap<>();
        this.freqMap = new HashMap<>();
        this.minFreq = 1;
    }

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K, V> result = null;
        final ICache<K,V> cache = context.cache();
        // 超过限制，移除频次最低的元素
        if(cache.size() >= context.size()) {
            FreqNode<K,V> evictNode = this.getMinFreqNode();
            K evictKey = evictNode.key();
            V evictValue = cache.remove(evictKey);

            log.debug("淘汰最小频率信息, key: {}, value: {}, freq: {}",
                    evictKey, evictValue, evictNode.frequency());
            result = new CacheEntry<>(evictKey, evictValue);
        }

        return result;
    }

    /**
     * 获取最小频率的 节点
     *
     * @return 结果
     * @since 0.0.14
     */
    private FreqNode<K, V> getMinFreqNode() {
        LinkedHashSet<FreqNode<K,V>> set = freqMap.get(minFreq);

        if(CollectionUtil.isNotEmpty(set)) {
            return set.iterator().next();
        }

        throw new CacheRuntimeException("未发现最小频率的 Key");
    }


    /**
     * 更新元素，更新 minFreq 信息
     * @param key 元素
     * @since 0.0.14
     */
    @Override
    public void updateKey(final K key) {
        FreqNode<K,V> freqNode = keyMap.get(key);

        //1. 已经存在
        if(ObjectUtil.isNotNull(freqNode)) {
            //1.1 移除原始的节点信息
            int frequency = freqNode.frequency();
            LinkedHashSet<FreqNode<K,V>> oldSet = freqMap.get(frequency);
            oldSet.remove(freqNode);
            //1.2 更新最小数据频率
            if (minFreq == frequency && oldSet.isEmpty()) {
                minFreq++;
                log.debug("minFreq 增加为：{}", minFreq);
            }
            //1.3 更新频率信息
            frequency++;
            freqNode.frequency(frequency);
            //1.4 放入新的集合
            this.addToFreqMap(frequency, freqNode);
        } else {
            //2. 不存在
            //2.1 构建新的元素
            FreqNode<K,V> newNode = new FreqNode<>(key);

            //2.2 固定放入到频率为1的列表中
            this.addToFreqMap(1, newNode);

            //2.3 更新 minFreq 信息
            this.minFreq = 1;

            //2.4 添加到 keyMap
            this.keyMap.put(key, newNode);
        }
    }

    /**
     * 加入到频率 MAP
     * @param frequency 频率
     * @param freqNode 节点
     */
    private void addToFreqMap(final int frequency, FreqNode<K,V> freqNode) {
        LinkedHashSet<FreqNode<K,V>> set = freqMap.get(frequency);
        if (set == null) {
            set = new LinkedHashSet<>();
        }
        set.add(freqNode);
        freqMap.put(frequency, set);
        log.debug("freq={} 添加元素节点：{}", frequency, freqNode);
    }

    /**
     * 移除元素
     *
     * 1. 从 freqMap 中移除
     * 2. 从 keyMap 中移除
     * 3. 更新 minFreq 信息
     *
     * @param key 元素
     * @since 0.0.14
     */
    @Override
    public void removeKey(final K key) {
        FreqNode<K,V> freqNode = this.keyMap.remove(key);

        //1. 根据 key 获取频率
        int freq = freqNode.frequency();
        LinkedHashSet<FreqNode<K,V>> set = this.freqMap.get(freq);

        //2. 移除频率中对应的节点
        set.remove(freqNode);
        log.debug("freq={} 移除元素节点：{}", freq, freqNode);

        //3. 更新 minFreq
        if(CollectionUtil.isEmpty(set) && minFreq == freq) {
            minFreq--;
            log.debug("minFreq 降低为：{}", minFreq);
        }
    }

}
