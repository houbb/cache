package com.github.houbb.cache.core.support.persist.impl;

import com.alibaba.fastjson.JSON;
import com.github.houbb.cache.api.ICachePersistAofEntry;
import com.github.houbb.cache.core.support.persist.AbstractCachePersistAof;
import com.github.houbb.heaven.util.io.FileUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 缓存持久化-AOF 持久化模式
 * @author binbin.hou
 * @since 0.0.10
 */
public class CachePersistAof<K,V> extends AbstractCachePersistAof<K,V> {

    private static final Log log = LogFactory.getLog(CachePersistAof.class);

    /**
     * 缓存列表
     * @since 0.0.10
     */
    private final List<String> bufferList = new ArrayList<>();

    /**
     * 数据持久化路径
     * @since 0.0.10
     */
    private final String dbPath;

    public CachePersistAof(String dbPath) {
        this.dbPath = dbPath;
    }

    /**
     * 添加文件内容到 buffer 列表中
     * @param aofEntry entry 信息
     * @since 0.0.10
     */
    @Override
    public boolean append(final ICachePersistAofEntry aofEntry) {
        if(aofEntry != null) {
            String json = JSON.toJSONString(aofEntry);
            log.debug("[Cache] AOF append json={}", json);
            bufferList.add(json);

            return true;
        }

        return false;
    }

    @Override
    protected void doPersist() {
        log.info("[Cache] 开始 AOF 持久化到文件");
        // 1. 创建文件
        if(!FileUtil.exists(dbPath)) {
            FileUtil.createFile(dbPath);
        }
        // 2. 持久化追加到文件中
        FileUtil.append(dbPath, bufferList);

        // 3. 清空 buffer 列表
        bufferList.clear();
        log.info("[Cache] 完成 AOF 持久化到文件");
    }

}
