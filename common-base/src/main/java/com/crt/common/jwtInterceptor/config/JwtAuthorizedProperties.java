package com.crt.common.jwtInterceptor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 自定义属性文件映射类
 */
@Component
@PropertySource(value = {"classpath:jwtsetting.properties"},
        ignoreResourceNotFound = true, encoding = "UTF-8", name = "jwtsetting.properties")
public class JwtAuthorizedProperties {

    /** 定义token 的头部属性key*/
    @Value("${jwt.auth.header}")
    private String tokenHeaderKey;

    /** JWT 自定义秘钥定义*/
    @Value("${jwt.auth.secret}")
    private String jwtCustomSecret;

    /** JWT签名有效期，默认8小时，480分钟*/
    @Value("${jwt.auth.expiration}")
    private Long jwtExpirationTimeDefult;

    public void setTokenHeaderKey(String tokenHeaderKey) {
        this.tokenHeaderKey = tokenHeaderKey;
    }

    public void setJwtExpirationTimeDefult(Long jwtExpirationTimeDefult) {
        this.jwtExpirationTimeDefult = jwtExpirationTimeDefult;
    }

    public void setJwtCustomSecret(String jwtCustomSecret) {
        this.jwtCustomSecret = jwtCustomSecret;
    }

    public String getTokenHeaderKey() {
        return tokenHeaderKey;
    }

    public Long getJwtExpirationTimeDefult() {
        return jwtExpirationTimeDefult;
    }

    public String getJwtCustomSecret() {
        return jwtCustomSecret;
    }
}
