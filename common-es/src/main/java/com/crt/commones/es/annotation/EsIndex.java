package com.crt.commones.es.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * mark a class es index
 *
 * @author dreamingo
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EsIndex {
    String name();

    String config() default "";

    int shards() default 5;

    int replicas() default 3;

    String interval() default "5s";
}
