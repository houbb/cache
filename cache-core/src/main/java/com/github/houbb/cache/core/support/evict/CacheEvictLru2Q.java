package com.github.houbb.cache.core.support.evict;

import com.github.houbb.cache.api.ICache;
import com.github.houbb.cache.api.ICacheEntry;
import com.github.houbb.cache.api.ICacheEvictContext;
import com.github.houbb.cache.core.exception.CacheRuntimeException;
import com.github.houbb.cache.core.model.CacheEntry;
import com.github.houbb.cache.core.model.DoubleListNode;
import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * 淘汰策略-LRU 最近最少使用
 *
 * 实现方式：Lru + FIFO
 * @author binbin.hou
 * @since 0.0.13
 */
public class CacheEvictLru2Q<K,V> extends AbstractCacheEvict<K,V> {

    private static final Log log = LogFactory.getLog(CacheEvictLru2Q.class);

    /**
     * 队列大小限制
     *
     * 降低 O(n) 的消耗，避免耗时过长。
     * @since 0.0.13
     */
    private static final int LIMIT_QUEUE_SIZE = 1024;

    /**
     * 第一次访问的队列
     * @since 0.0.13
     */
    private Queue<K> firstQueue;

    /**
     * 头结点
     * @since 0.0.13
     */
    private DoubleListNode<K,V> head;

    /**
     * 尾巴结点
     * @since 0.0.13
     */
    private DoubleListNode<K,V> tail;

    /**
     * map 信息
     *
     * key: 元素信息
     * value: 元素在 list 中对应的节点信息
     * @since 0.0.13
     */
    private Map<K, DoubleListNode<K,V>> lruIndexMap;

    public CacheEvictLru2Q() {
        this.firstQueue = new LinkedList<>();
        this.lruIndexMap = new HashMap<>();
        this.head = new DoubleListNode<>();
        this.tail = new DoubleListNode<>();

        this.head.next(this.tail);
        this.tail.pre(this.head);
    }

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K, V> result = null;
        final ICache<K,V> cache = context.cache();
        // 超过限制，移除队尾的元素
        if(cache.size() >= context.size()) {
            K evictKey = null;

            //1. firstQueue 不为空，优先移除队列中元素
            if(!firstQueue.isEmpty()) {
                evictKey = firstQueue.remove();
            } else {
                // 获取尾巴节点的前一个元素
                DoubleListNode<K,V> tailPre = this.tail.pre();
                if(tailPre == this.head) {
                    log.error("当前列表为空，无法进行删除");
                    throw new CacheRuntimeException("不可删除头结点!");
                }

                evictKey = tailPre.key();
            }

            // 执行移除操作
            V evictValue = cache.remove(evictKey);
            result = new CacheEntry<>(evictKey, evictValue);
        }

        return result;
    }


    /**
     * 放入元素
     * 1. 如果 lruIndexMap 已经存在，则处理 lru 队列，先删除，再插入。
     * 2. 如果 firstQueue 中已经存在，则处理 first 队列，先删除 firstQueue，然后插入 Lru。
     * 1 和 2 是不同的场景，但是代码实际上是一样的，删除逻辑中做了二种场景的兼容。
     *
     * 3. 如果不在1、2中，说明是新元素，直接插入到 firstQueue 的开始即可。
     *
     * @param key 元素
     * @since 0.0.13
     */
    @Override
    public void updateKey(final K key) {
        //1.1 是否在 LRU MAP 中
        //1.2 是否在 firstQueue 中
        DoubleListNode<K,V> node = lruIndexMap.get(key);
        if(ObjectUtil.isNotNull(node)
            || firstQueue.contains(key)) {
            //1.3 删除信息
            this.removeKey(key);

            //1.4 加入到 LRU 中
            this.addToLruMapHead(key);
            return;
        }

        //2. 直接加入到 firstQueue 队尾
//        if(firstQueue.size() >= LIMIT_QUEUE_SIZE) {
//            // 避免第一次访问的列表一直增长，移除队头的元素
//            firstQueue.remove();
//        }
        firstQueue.add(key);
    }

    /**
     * 插入到 LRU Map 头部
     * @param key 元素
     * @since 0.0.13
     */
    private void addToLruMapHead(final K key) {
        //2. 新元素插入到头部
        //head<->next
        //变成：head<->new<->next
        DoubleListNode<K,V> newNode = new DoubleListNode<>();
        newNode.key(key);

        DoubleListNode<K,V> next = this.head.next();
        this.head.next(newNode);
        newNode.pre(this.head);
        next.pre(newNode);
        newNode.next(next);

        //2.2 插入到 map 中
        lruIndexMap.put(key, newNode);
    }

    /**
     * 移除元素
     *
     * 1. 获取 map 中的元素
     * 2. 不存在直接返回，存在执行以下步骤：
     * 2.1 删除双向链表中的元素
     * 2.2 删除 map 中的元素
     *
     * @param key 元素
     * @since 0.0.13
     */
    @Override
    public void removeKey(final K key) {
        DoubleListNode<K,V> node = lruIndexMap.get(key);

        //1. LRU 删除逻辑
        if(ObjectUtil.isNotNull(node)) {
            // A<->B<->C
            // 删除 B，需要变成： A<->C
            DoubleListNode<K,V> pre = node.pre();
            DoubleListNode<K,V> next = node.next();

            pre.next(next);
            next.pre(pre);

            // 删除 map 中对应信息
            this.lruIndexMap.remove(node.key());
        } else {
            //2. FIFO 删除逻辑（O(n) 时间复杂度）
            firstQueue.remove(key);
        }
    }

}
