package com.github.houbb.cache.core.load;

import com.github.houbb.cache.core.support.load.AbstractCacheLoad;

/**
 * @author binbin.hou
 * @since 0.0.7
 */
public class MyCacheLoad extends AbstractCacheLoad<String,String> {

    @Override
    public void doLoad() {
        super.context.map().put("1", "1");
        super.context.map().put("2", "2");
    }


}
