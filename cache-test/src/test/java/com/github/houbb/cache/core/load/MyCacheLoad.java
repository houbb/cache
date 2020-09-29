package com.github.houbb.cache.core.load;

import com.github.houbb.cache.api.ICache;
import com.github.houbb.cache.api.ICacheLoad;

/**
 * @author binbin.hou
 * @since 0.0.7
 */
public class MyCacheLoad implements ICacheLoad<String,String> {

    @Override
    public void load(ICache<String, String> cache) {
        cache.put("1", "1");
        cache.put("2", "2");
    }

}
