package com.github.houbb.cache.core.util;

import com.github.houbb.heaven.util.util.CollectionUtil;

import java.util.List;

/**
 * @since 1.0.0
 */
public class InnerArrayUtil {

    public static Object[] listToArray(List<Object> list) {
        if(CollectionUtil.isEmpty(list)) {
            return null;
        }

        Object[] array = new Object[list.size()];

        for(int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

}
