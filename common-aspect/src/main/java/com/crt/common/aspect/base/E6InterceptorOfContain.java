package com.crt.common.aspect.base;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.core.Ordered;

/**
 * @Author liupengfei@e6yun.com
 * @Date 2019/3/29 11:22
 * @Description 定义所有在AllInOneAspect中要执行的拦截器任务
 * 实现Ordered接口，为了可以定义执行次序
 * 所有E6Interceptor执行次序为：
 * E6Interceptor1.doBefore
 *  E6Interceptor2.doBefore
 *     源方法执行()
 *  E6Interceptor2.doAfter
 * E6Interceptor1.doAfter
 **/
public interface E6InterceptorOfContain extends Ordered{

    /**
     * 是否包含这个方法签名
     * @param className 类名全路径
     * @param methodName 方法名
     * @return
     */
    boolean containsSignature(String className, String methodName);
    /**
     * 执行拦截任务,在源方法执行前
     * @param className 类名全路径
     * @param methodName 方法名
     * @param joinPoint 切面参数
     */
    Object doBefore(String className, String methodName, ProceedingJoinPoint joinPoint) throws Throwable;

    /**
     * 执行拦截任务,在源方法执行后
     * @param className 类名全路径
     * @param methodName 方法名
     * @param joinPoint 切面参数
     * @param beforeResult 执行before时返回的对象，用来传递一些参数
     */
    default void doAfter(String className, String methodName, ProceedingJoinPoint joinPoint, Object beforeResult){
    }


    /**
     * 默认的优先级别为最低
     * @return
     */
    @Override
    default int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
