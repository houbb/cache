package com.github.houbb.cache.core.support.evict;

import com.github.houbb.cache.api.*;
import com.github.houbb.cache.core.constant.enums.CacheRemoveType;
import com.github.houbb.cache.core.support.listener.remove.CacheRemoveListenerContext;
import com.github.houbb.heaven.util.lang.ObjectUtil;

import java.util.List;

/**
 * 丢弃策略-抽象实现类
 * @author binbin.hou
 * @since 0.0.11
 */
public abstract class AbstractCacheEvict<K,V> implements ICacheEvict<K,V> {

    @Override
    public ICacheEntry<K,V> evict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K,V> evictEntry = doEvict(context);

        // 添加拦截器调用
        if(ObjectUtil.isNotNull(evictEntry)) {
            // 执行淘汰监听器
            ICacheRemoveListenerContext<K,V> removeListenerContext = CacheRemoveListenerContext.<K,V>newInstance().key(evictEntry.key())
                    .value(evictEntry.value())
                    .type(CacheRemoveType.EVICT.code());
            for(ICacheRemoveListener<K,V> listener : context.cache().removeListeners()) {
                listener.listen(removeListenerContext);
            }
        }

        //3. 返回结果
        return evictEntry;
    }

    /**
     * 执行移除
     * @param context 上下文
     * @return 结果
     * @since 0.0.11
     */
    protected abstract ICacheEntry<K,V> doEvict(ICacheEvictContext<K, V> context);

    @Override
    public void update(K key) {

    }

    @Override
    public void remove(K key) {

    }
}
