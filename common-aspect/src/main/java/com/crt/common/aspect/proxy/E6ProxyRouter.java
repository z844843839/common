package com.crt.common.aspect.proxy;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @Author liupengfei@e6yun.com
 * @Date 2019/5/16 14:16
 * @Description 方法拦截Router，用于多租户场景下功能扩展点的不同实现选择
 **/
public interface E6ProxyRouter {
    /**
     * 获取一个代理
     * @param className 全路径类名
     * @param joinPoint 切入点对象，可以拿到参数，做更加灵活的动态的代理判断
     * @return
     */
    Object getProxy(String className, ProceedingJoinPoint joinPoint);
}
