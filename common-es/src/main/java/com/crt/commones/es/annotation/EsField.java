package com.crt.commones.es.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ES Field config annotation
 *
 * @author dreamingo
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface EsField {
    String type();

    String rawType() default "";

    String analyzer() default "";

    boolean innerFieldsKeyword() default false;

    boolean fieldData() default false;
}
