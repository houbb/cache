package com.github.houbb.cache.core.support.load.impl;

import com.alibaba.fastjson.JSON;
import com.github.houbb.cache.api.ICacheExpire;
import com.github.houbb.cache.api.ICacheMap;
import com.github.houbb.cache.core.model.PersistRdbEntry;
import com.github.houbb.cache.core.support.load.AbstractCacheLoad;
import com.github.houbb.heaven.util.io.FileUtil;
import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.util.List;

/**
 * 加载策略-文件路径
 * @author binbin.hou
 * @since 0.0.8
 */
public class CacheLoadDbJson<K,V> extends AbstractCacheLoad<K,V> {

    private static final Log log = LogFactory.getLog(CacheLoadDbJson.class);

    /**
     * 文件路径
     * @since 0.0.8
     */
    private final String dbPath;

    public CacheLoadDbJson(String dbPath) {
        this.dbPath = dbPath;
    }

    @Override
    public void doLoad() {
        List<String> lines = FileUtil.readAllLines(dbPath);
        log.info("[load] 开始处理 path: {}", dbPath);
        if(CollectionUtil.isEmpty(lines)) {
            log.info("[load] path: {} 文件内容为空，直接返回", dbPath);
            return;
        }

        final ICacheMap<K,V> cacheMap = super.context.map();
        final ICacheExpire<K,V> cacheExpire = super.context.expire();
        for(String line : lines) {
            if(StringUtil.isEmpty(line)) {
                continue;
            }

            // 执行
            // 简单的类型还行，复杂的这种反序列化会失败
            PersistRdbEntry<K,V> entry = JSON.parseObject(line, PersistRdbEntry.class);

            K key = entry.getKey();
            V value = entry.getValue();
            Long expire = entry.getExpire();

            cacheMap.put(key, value);
            if(ObjectUtil.isNotNull(expire)) {
                cacheExpire.expireAt(key, expire);
            }
        }
        //nothing...
    }
}
