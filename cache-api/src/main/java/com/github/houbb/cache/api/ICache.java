package com.github.houbb.cache.api;

import java.util.List;
import java.util.Map;

/**
 * 缓存接口
 * @author binbin.hou
 * @since 0.0.1
 */
public interface ICache<K, V> extends Map<K, V> {

    /**
     * 设置过期时间
     * （1）如果 key 不存在，则什么都不做。
     * （2）暂时不提供新建 key 指定过期时间的方式，会破坏原来的方法。
     *
     * 会做什么：
     * 类似于 redis
     * （1）惰性删除。
     * 在执行下面的方法时，如果过期则进行删除。
     * {@link ICache#get(Object)} 获取
     * {@link ICache#values()} 获取所有值
     * {@link ICache#entrySet()} 获取所有明细
     *
     * 【数据的不一致性】
     * 调用其他方法，可能得到的不是使用者的预期结果，因为此时的 expire 信息可能没有被及时更新。
     * 比如
     * {@link ICache#isEmpty()} 是否为空
     * {@link ICache#size()} 当前大小
     * 同时会导致以 size() 作为过期条件的问题。
     *
     * 解决方案：考虑添加 refresh 等方法，暂时不做一致性的考虑。
     * 对于实际的使用，我们更关心 K/V 的信息。
     *
     * （2）定时删除
     * 启动一个定时任务。每次随机选择指定大小的 key 进行是否过期判断。
     * 类似于 redis，为了简化，可以考虑设定超时时间，频率与超时时间成反比。
     *
     * 其他拓展性考虑：
     * 后期考虑提供原子性操作，保证事务性。暂时不做考虑。
     * 此处默认使用 TTL 作为比较的基准，暂时不想支持 LastAccessTime 的淘汰策略。会增加复杂度。
     * 如果增加 lastAccessTime 过期，本方法可以不做修改。
     *
     * @param key         key
     * @param timeInMills 毫秒时间之后过期
     * @return this
     * @since 0.0.3
     */
    ICache<K, V> expire(final K key, final long timeInMills);

    /**
     * 在指定的时间过期
     * @param key key
     * @param timeInMills 时间戳
     * @return this
     * @since 0.0.3
     */
    ICache<K, V> expireAt(final K key, final long timeInMills);

    /**
     * 获取缓存的过期处理类
     * @return 处理类实现
     * @since 0.0.4
     */
    ICacheExpire<K,V> expire();

    /**
     * 删除监听类列表
     * @return 监听器列表
     * @since 0.0.6
     */
    List<ICacheRemoveListener<K,V>> removeListeners();

    /**
     * 慢日志监听类列表
     * @return 监听器列表
     * @since 0.0.9
     */
    List<ICacheSlowListener> slowListeners();

    /**
     * 加载信息
     * @return 加载信息
     * @since 0.0.7
     */
    ICacheLoad<K,V> load();

    /**
     * 持久化类
     * @return 持久化类
     * @since 0.0.10
     */
    ICachePersist<K,V> persist();

    /**
     * 淘汰策略
     * @return 淘汰
     * @since 0.0.11
     */
    ICacheEvict<K,V> evict();

}
