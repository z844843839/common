package com.crt.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 登陆cookie 自定义属性文件映射类
 */
@Component
@PropertySource(value = {"classpath:uaccookiesetting.properties"},
        ignoreResourceNotFound = true, encoding = "UTF-8", name = "uaccookiesetting.properties")
public class UacCookieProperties {

    @Value("${login.cookie.username.key}")
    private String cookieUsernameKey;

    @Value("${login.cookie.token.key}")
    private String cookieTokenKey;

    public void setCookieUsernameKey(String cookieUsernameKey) {
        this.cookieUsernameKey = cookieUsernameKey;
    }

    public void setCookieTokenKey(String cookieTokenKey) {
        this.cookieTokenKey = cookieTokenKey;
    }

    public String getCookieUsernameKey() {
        return cookieUsernameKey;
    }

    public String getCookieTokenKey() {
        return cookieTokenKey;
    }
}
