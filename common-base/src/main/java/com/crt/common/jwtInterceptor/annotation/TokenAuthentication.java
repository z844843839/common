package com.crt.common.jwtInterceptor.annotation;

import com.crt.common.jwtInterceptor.constant.AuthLevel;

import java.lang.annotation.*;

/**
 * 授权验证注解
 *
 * @author
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface TokenAuthentication {

    /** 授权验证名称说明 */
    public String name() default "token授权验证";

    /** 授权验证级别，默认开放无需授权验证*/
    public AuthLevel authLevel() default AuthLevel.NO_AUTH;
}
