package com.github.houbb.cache.core.support.persist.impl;

import com.alibaba.fastjson.JSON;
import com.github.houbb.cache.api.ICacheExpire;
import com.github.houbb.cache.core.model.PersistRdbEntry;
import com.github.houbb.cache.core.support.persist.AbstractCachePersist;
import com.github.houbb.heaven.util.io.FileUtil;

import java.nio.file.StandardOpenOption;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 缓存持久化-db-基于 JSON
 * @author binbin.hou
 * @since 0.0.8
 */
public class CachePersistDbJson<K,V> extends AbstractCachePersist<K,V> {

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
     */
    @Override
    public void doPersist() {
        Set<K> entrySet = super.persistContext.map().keySet();
        final ICacheExpire<K,V> expire = super.persistContext.expire();

        // 创建文件
        FileUtil.createFile(dbPath);
        // 清空文件
        FileUtil.truncate(dbPath);

        for(K key : entrySet) {
            Long expireTime = expire.expireTime(key);
            PersistRdbEntry<K,V> persistRdbEntry = new PersistRdbEntry<>();
            persistRdbEntry.setKey(key);
            persistRdbEntry.setValue(super.persistContext.map().get(key));
            persistRdbEntry.setExpire(expireTime);

            String line = JSON.toJSONString(persistRdbEntry);
            FileUtil.write(dbPath, line, StandardOpenOption.APPEND);
        }
    }

    @Override
    public long delay() {
        return 5;
    }

    @Override
    public long period() {
        return 5;
    }

    @Override
    public TimeUnit timeUnit() {
        return TimeUnit.MINUTES;
    }

}
