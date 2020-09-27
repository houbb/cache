package com.github.houbb.cache.core.bs;

import com.github.houbb.cache.api.ICache;
import org.junit.Assert;
import org.junit.Test;

/**
 * 缓存引导类测试
 * @author binbin.hou
 * @since 0.0.2
 */
public class CacheBsTest {

    /**
     * 大小指定测试
     * @since 0.0.2
     */
    @Test
    public void helloTest() {
        ICache<String, String> cache = CacheBs.<String,String>newInstance()
                .size(2)
                .build();

        cache.put("1", "1");
        cache.put("2", "2");
        cache.put("3", "3");
        cache.put("4", "4");

        Assert.assertEquals(2, cache.size());
        System.out.println(cache.keySet());
    }

}
