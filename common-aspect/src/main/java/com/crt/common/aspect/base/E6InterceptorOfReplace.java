package com.crt.common.aspect.base;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.core.Ordered;

/**
 * @Author liupengfei@e6yun.com
 * @Date 2019/5/16 11:22
 * @Description 定义所有在AllInOneAspect中要执行的拦截器任务
 * 实现Ordered接口，为了可以定义执行次序
 * 所有E6InterceptorOfReplace中只有优先级最高的一个可以执行
 **/
public interface E6InterceptorOfReplace extends Ordered{

    /**
     * 是否包含这个方法签名
     * @param className 类全路径
     * @param methodName 方法名
     * @param joinPoint 切面参数
     * @return
     */
    boolean containsSignature(String className, String methodName, ProceedingJoinPoint joinPoint);
    /**
     * 执行拦截任务,在源方法执行前
     * @param className 类全路径
     * @param methodName 方法名
     * @param joinPoint 切面参数
     */
    Object doMethod(String className, String methodName, ProceedingJoinPoint joinPoint) throws Throwable;

    /**
     * 默认的优先级别为最低
     * @return
     */
    @Override
    default int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
