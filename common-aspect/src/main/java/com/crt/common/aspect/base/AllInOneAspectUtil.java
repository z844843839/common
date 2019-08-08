package com.crt.common.aspect.base;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author liupengfei@e6yun.com
 * @Date 2019/3/13 8:46
 * @Description 切面工具 使用时，需要手工配置切面，然后执行这个工具类的invoke方法
 **/
@Component
public class AllInOneAspectUtil {
    private static Logger logger = LoggerFactory.getLogger(AllInOneAspectUtil.class);

    /**
     * 代替型
     */
    @Autowired(required = false)
    List<E6InterceptorOfReplace> e6InterceptorOfReplaces;
    /**
     * 包含型
     */
    @Autowired(required = false)
    List<E6InterceptorOfContain> e6InterceptorOfContains;

    static boolean init = false;

    /**
     * 拦截器内容
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    public Object invoke(ProceedingJoinPoint joinPoint) throws Throwable {
        if (e6InterceptorOfContains == null && e6InterceptorOfReplaces==null) {
            return joinPoint.proceed();
        }
        if (!init) {
            if(e6InterceptorOfContains!=null) {
                OrderComparator.sort(e6InterceptorOfContains);
            }
            if(e6InterceptorOfReplaces!=null) {
                OrderComparator.sort(e6InterceptorOfReplaces);
            }
            init = true;
        }
        Signature signature = joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();

        if(e6InterceptorOfReplaces!=null){
            for (E6InterceptorOfReplace e6Interceptor : e6InterceptorOfReplaces) {
                if (e6Interceptor.containsSignature(className, methodName, joinPoint)) {
                    //这里一般来说只会有一个，如果有多个就只能优先执行第一个了
                    return e6Interceptor.doMethod(className, methodName, joinPoint);
                }
            }
        }

        if(e6InterceptorOfContains==null || e6InterceptorOfContains.isEmpty()){
            return joinPoint.proceed();
        }
        //找出拦截器中对当前方法签名生效的部分
        List<E6InterceptorOfContain> e6InterceptorContainsUse = new ArrayList<>(e6InterceptorOfContains.size());
        for (E6InterceptorOfContain e6Interceptor : e6InterceptorOfContains) {
            if (e6Interceptor.containsSignature(className,methodName)) {
                e6InterceptorContainsUse.add(e6Interceptor);
            }
        }
        if (e6InterceptorContainsUse.isEmpty()) {
            return joinPoint.proceed();
        }
        //这个list用于临时存储doBefore执行结果
        List<Object> beforeMethodResult = new ArrayList<>(e6InterceptorContainsUse.size());
        //依次执行doBefore方法
        for (E6InterceptorOfContain e6Interceptor : e6InterceptorContainsUse) {
            beforeMethodResult.add(e6Interceptor.doBefore(className,methodName, joinPoint));
        }
        //调用源方法
        Object object = joinPoint.proceed();
        //倒序执行doAfter方法
        for (int i = e6InterceptorContainsUse.size() - 1; i >= 0; i--) {
            e6InterceptorContainsUse.get(i).doAfter(className,methodName, joinPoint, beforeMethodResult.get(i));
        }
        return object;
    }
}
