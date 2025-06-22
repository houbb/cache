package com.github.houbb.cache.core.support.evict;

import com.github.houbb.cache.api.ICacheContext;
import com.github.houbb.cache.api.ICacheEntry;
import com.github.houbb.cache.api.ICacheEvict;

/**
 * 丢弃策略-抽象实现类
 * @author binbin.hou
 * @since 0.0.11
 */
public abstract class AbstractCacheEvict<K,V> implements ICacheEvict<K,V> {

    /**
     * 执行移除
     * @param context 上下文
     * @param newKey 当前 key
     * @return 结果
     * @since 0.0.11
     */
    protected abstract ICacheEntry<K,V> doEvict(ICacheContext<K, V> context, final K newKey);

    @Override
    public ICacheEntry<K,V> evict(ICacheContext<K, V> context, final K newKey) {
        //3. 返回结果
        return doEvict(context, newKey);
    }

    /**
     * 是否需要驱逐
     * @param context 上下文
     * @return 结果
     */
    protected boolean isNeedEvict(ICacheContext<K, V> context) {
        return context.map().size() >= context.size();
    }

    /**
     * 执行驱逐删除
     * @param context 上下文
     * @param key 键
     * @return 结果
     */
    protected V doEvictRemove(final ICacheContext<K, V> context,
                              final K key) {
        return context.map().remove(key);
    }


    @Override
    public void updateKey(ICacheContext<K, V> context, K key) {

    }

    @Override
    public void removeKey(ICacheContext<K, V> context, K key) {

    }

}
