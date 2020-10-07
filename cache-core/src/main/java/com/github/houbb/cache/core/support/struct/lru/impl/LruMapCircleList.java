package com.github.houbb.cache.core.support.struct.lru.impl;

import com.github.houbb.cache.api.ICacheEntry;
import com.github.houbb.cache.core.exception.CacheRuntimeException;
import com.github.houbb.cache.core.model.CacheEntry;
import com.github.houbb.cache.core.model.CircleListNode;
import com.github.houbb.cache.core.support.struct.lru.ILruMap;
import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于循环列表的实现
 * @author binbin.hou
 * @since 0.0.15
 */
public class LruMapCircleList<K,V> implements ILruMap<K,V> {

    private static final Log log = LogFactory.getLog(LruMapCircleList.class);

    /**
     * 头结点
     * @since 0.0.15
     */
    private CircleListNode<K,V> head;

    /**
     * 映射 map
     * @since 0.0.15
     */
    private Map<K, CircleListNode<K,V>> indexMap;

    public LruMapCircleList() {
        // 双向循环链表
        this.head = new CircleListNode<>(null);
        this.head.next(this.head);
        this.head.pre(this.head);

        indexMap = new HashMap<>();
    }

    /**
     * 删除最老的元素
     *
     * （1）从 head.next 开始遍历，如果元素 accessFlag = 0，则直接移除
     * （2）如果 accessFlag=1，则设置其值为0，循环下一个节点。
     *
     * @return 结果
     * @since 0.0.15
     */
    @Override
    public ICacheEntry<K, V> removeEldest() {
        //fast-fail
        if(isEmpty()) {
            log.error("当前列表为空，无法进行删除");
            throw new CacheRuntimeException("不可删除头结点!");
        }

        // 从最老的元素开始，此处直接从 head.next 开始，后续可以考虑优化记录这个 key
        CircleListNode<K,V> node = this.head;
        while (node.next() != this.head) {
            // 下一个元素
            node = node.next();

            if(!node.accessFlag()) {
                // 未访问，直接淘汰
                K key = node.key();

                this.removeKey(key);
                return CacheEntry.of(key, node.value());
            } else {
                // 设置当前 accessFlag = 0,继续下一个
                node.accessFlag(false);
            }
        }

        // 如果循环一遍都没找到，直接取第一个元素即可。
        CircleListNode<K,V> firstNode = this.head.next();
        return CacheEntry.of(firstNode.key(), firstNode.value());
    }

    /**
     * 放入元素
     *
     * 类似于 FIFO，直接放在队列的最后
     *
     * head==1==head
     * 加入元素：
     *
     * head==1==2==head
     *
     * （1）如果元素不存在，则直接插入。
     * 默认 accessFlag = 0;
     * （2）如果已经存在，则更新 accessFlag=1;
     *
     * @param key 元素
     * @since 0.0.15
     */
    @Override
    public void updateKey(final K key) {
        CircleListNode<K,V> node = indexMap.get(key);

        // 存在
        if(ObjectUtil.isNotNull(node)) {
            node.accessFlag(true);
            log.debug("节点已存在，设置节点访问标识为 true, key: {}", key);
        } else {
            // 不存在，则插入到最后
            node = new CircleListNode<>(key);

            CircleListNode<K,V> tail = head.pre();
            tail.next(node);
            node.pre(tail);
            node.next(head);
            head.pre(node);

            // 放入 indexMap 中，便于快速定位
            indexMap.put(key, node);
            log.debug("节点不存在，新增节点到链表中：{}", key);
        }
    }

    /**
     * 移除元素
     *
     * 1. 是否存在，不存在则忽略
     * 2. 存在则移除，从链表+map中移除
     *
     * head==1==2==head
     *
     * 删除 2 之后：
     * head==1==head
     * @param key 元素
     * @since 0.0.15
     */
    @Override
    public void removeKey(final K key) {
        CircleListNode<K,V> node = indexMap.get(key);
        if(ObjectUtil.isNull(node)) {
            log.warn("对应的删除信息不存在：{}", key);
            return;
        }

        CircleListNode<K,V> pre = node.pre();
        CircleListNode<K,V> next = node.next();

        //1-->(x2)-->3  直接移除2
        pre.next(next);
        next.pre(pre);
        indexMap.remove(key);

        log.debug("Key: {} 从循环链表中移除", key);
    }

    @Override
    public boolean isEmpty() {
        return indexMap.isEmpty();
    }

    @Override
    public boolean contains(K key) {
        return indexMap.containsKey(key);
    }

}
