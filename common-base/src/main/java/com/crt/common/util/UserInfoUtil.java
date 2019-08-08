package com.crt.common.util;

import com.crt.common.config.UacCookieProperties;
import com.crt.common.redis.RedisUtil;
import com.crt.common.vo.E6Wrapper;
import com.crt.common.vo.E6WrapperUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 用户信息获取工具类
 */
@Component
public class UserInfoUtil {

    @Autowired
    private UacCookieProperties uacCookieProperties;

    private static  UacCookieProperties uacCookieProperties1;
    ;

    @Autowired
    private RedisUtil<Map<String,Object>> cache;

    private static  RedisUtil<Map<String,Object>> userCache;

    @PostConstruct
    public void beforeInit() {
        userCache = cache;
        uacCookieProperties1 = uacCookieProperties;

    }

    /**
     * 获取用户信息方法
     * @return redis里面存储的用户信息
     */
    public static E6Wrapper getUserInfo() {

        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token))
        {
            return E6WrapperUtil.error("token不存在,用户信息获取失败");
        }
        else
        {
            Map<String,Object> result = userCache.get(token);
            if (result == null || result.isEmpty())
            {
                return E6WrapperUtil.error("token错误,用户信息获取失败");
            }
            else
            {
                return E6WrapperUtil.ok(result);
            }

        }
    }

}
