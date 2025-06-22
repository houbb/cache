package com.github.houbb.cache.core.support.load.impl;

import com.alibaba.fastjson.JSON;
import com.github.houbb.cache.api.*;
import com.github.houbb.cache.core.model.PersistAofEntry;
import com.github.houbb.cache.core.support.load.AbstractCacheLoad;
import com.github.houbb.heaven.util.io.FileUtil;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.heaven.util.lang.reflect.ReflectMethodUtil;
import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 加载策略-AOF文件模式
 * @author binbin.hou
 * @since 0.0.10
 */
public class CacheLoadAof<K,V> extends AbstractCacheLoad<K,V> {

    private static final Log log = LogFactory.getLog(CacheLoadAof.class);

    /**
     * 方法缓存
     *
     * 暂时比较简单，直接通过方法判断即可，不必引入参数类型增加复杂度。
     * @since 0.0.10
     */
    private final Map<String, Method> MAP_METHODS = new HashMap<>();
    private final Map<String, Method> EXPIRE_METHODS = new HashMap<>();

    @Override
    public void init(ICacheLoadContext<K, V> context) {
        super.init(context);

        // 初始化对应的方法信息
        final ICacheMap<K,V> map = context.map();
        // map
        Method[] methodsForMap = map.getClass().getMethods();
        for(Method method : methodsForMap) {
            MAP_METHODS.put(method.getName(), method);
        }

        // expire
        final ICacheExpire<K,V> expire = context.expire();
        Method[] methodsForExpire = expire.getClass().getMethods();
        for(Method method : methodsForExpire) {
            EXPIRE_METHODS.put(method.getName(), method);
        }
    }

    /**
     * 文件路径
     * @since 0.0.8
     */
    private final String dbPath;

    public CacheLoadAof(String dbPath) {
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

        final ICacheMap<K,V> map = context.map();
        final ICacheExpire<K,V> expire = context.expire();

        for(String line : lines) {
            if(StringUtil.isEmpty(line)) {
                continue;
            }

            // 执行
            // 简单的类型还行，复杂的这种反序列化会失败
            PersistAofEntry entry = JSON.parseObject(line, PersistAofEntry.class);

            final String methodName = entry.getMethodName();
            final Object[] objects = entry.getParams();

            // expire
            Method expireMethod = EXPIRE_METHODS.get(methodName);
            if(expireMethod != null) {
                ReflectMethodUtil.invoke(expire, expireMethod, objects);
            }

            // map
            Method mapMethod = MAP_METHODS.get(methodName);
            if(mapMethod != null) {
                ReflectMethodUtil.invoke(map, mapMethod, objects);
            }
        }
    }


}
