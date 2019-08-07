package com.crt.common.jwtInterceptor.constant;

/**
 * 授权验证层级
 *
 */
public enum AuthLevel {
    /**
     * 开放，不验证
     */
    NO_AUTH,

    /**
     * 身份认证
     */
    LOGIN,
}
