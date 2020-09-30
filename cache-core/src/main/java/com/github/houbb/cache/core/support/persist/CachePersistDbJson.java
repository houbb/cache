package com.github.houbb.cache.core.support.persist;

import com.alibaba.fastjson.JSON;
import com.github.houbb.cache.api.ICache;
import com.github.houbb.cache.api.ICachePersist;
import com.github.houbb.cache.core.model.PersistEntry;
import com.github.houbb.heaven.util.io.FileUtil;
import com.github.houbb.heaven.util.lang.StringUtil;

import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Set;

/**
 * 缓存持久化-db-基于 JSON
 * @author binbin.hou
 * @since 0.0.8
 */
public class CachePersistDbJson<K,V> implements ICachePersist<K,V> {

    /**
     * 数据库路径
     * @since 0.0.8
     */
    private final String dbPath;

    public CachePersistDbJson(String dbPath) {
        this.dbPath = dbPath;
    }

    /**
     * 持久化
     * key长度 key+value
     * 第一个空格，获取 key 的长度，然后截取
     * @param cache 缓存
     */
    @Override
    public void persist(ICache<K, V> cache) {
        Set<Map.Entry<K,V>> entrySet = cache.entrySet();

        // 创建文件
        FileUtil.createFile(dbPath);
        // 清空文件
        FileUtil.truncate(dbPath);

        for(Map.Entry<K,V> entry : entrySet) {
            K key = entry.getKey();
            Long expireTime = cache.expire().expireTime(key);
            PersistEntry<K,V> persistEntry = new PersistEntry<>();
            persistEntry.setKey(key);
            persistEntry.setValue(entry.getValue());
            persistEntry.setExpire(expireTime);

            String line = JSON.toJSONString(persistEntry);
            FileUtil.write(dbPath, line, StandardOpenOption.APPEND);
        }
    }

}
