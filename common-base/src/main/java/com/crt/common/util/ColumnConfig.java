package com.crt.common.util;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description 用于配置实体类字段说明信息
 * @author malin
 * @date 2020年06月30日
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnConfig {
    /**
     * 字段的中文名
     * @return
     */
    String description() default "";
}
