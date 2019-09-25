package com.crt.common.sqlInterceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解
 * @author malin
 */
@Target({ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface  InterceptAnnotation {

    boolean flag() default true;

    QueryAuthSqlType queryType();

    /**
     *
     */
     enum QueryAuthSqlType{
        /**
         * 集合查询
         */
        QUERY_LIMIT,
        /**
         * 数量查询
         */
        QUERY_COUNT;
    }
}

