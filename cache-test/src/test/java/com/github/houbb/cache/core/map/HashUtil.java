package com.github.houbb.cache.core.map;

/**
 * hash 工具类
 * @author binbin.hou
 * @since 0.0.1
 */
public final class HashUtil {

    private HashUtil(){}

    /**
     * Retrieve object hash code and applies a supplemental hash function to the
     * result hash, which defends against poor quality hash functions.  This is
     * critical because HashMap uses power-of-two length hash tables, that
     * otherwise encounter collisions for hashCodes that do not differ
     * in lower bits. Note: Null keys always map to hash 0, thus index 0.
     * @param object 对象
     * @return hash 結果
     */
    public static int hash(Object object) {
        if(object == null) {
            return 0;
        }
        //TODO: 添加 hash 处理
        return object.hashCode();
    }

    /**
     * Returns index for hash code h.
     * @param h hash 值
     * @param length 整体长度
     * @return hash 結果
     */
    public static int indexFor(int h, int length) {
        return h % length;
    }

}
