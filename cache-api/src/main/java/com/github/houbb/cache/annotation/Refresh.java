package com.github.houbb.cache.annotation;

import java.lang.annotation.*;

/**
 * 刷新所有信息
 * @author binbin.hou
 * @since 0.0.4
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Refresh {
}
