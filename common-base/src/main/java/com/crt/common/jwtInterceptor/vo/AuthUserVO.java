package com.crt.common.jwtInterceptor.vo;

/**
 * 授权用户信息VO
 */
public class AuthUserVO {

    /** 用户唯一标识*/
    private String username;

    /** 客户端代理用户信息*/
    private String userAgent;

    /** 自定义属性*/
    public String customAttr;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserAgent() { return userAgent; }

    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    public String getCustomAttr() {
        return customAttr;
    }

    public void setCustomAttr(String customAttr) {
        this.customAttr = customAttr;
    }

}
