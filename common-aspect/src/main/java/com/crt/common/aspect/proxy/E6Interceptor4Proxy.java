package com.crt.common.aspect.proxy;

import com.crt.common.aspect.base.E6InterceptorOfReplace;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author liupengfei@e6yun.com
 * @Date 2019/5/16 14:20
 * @Description 扩展拦截器，使用时要用Configuration类手工配置
 * 【动态选择代理，用于SaaS多租户】如果一个类可能存在多个代理，根据运行时数据来选择，那么一定要用Router类，程序动态控制由哪个Proxy执行
 * 【静态选择代理，用于不同项目的区分】如果一个类就一个代理，那么就只需要一个Proxy就行了，不用建Router类，框架自动会使用这个Proxy
 **/
public class E6Interceptor4Proxy implements E6InterceptorOfReplace {
    static Logger logger = LoggerFactory.getLogger(E6Interceptor4Proxy.class);

    BeanFactory beanFactory;

    /**
     * Router 后缀
     */
    public final static String ROUTER_SUFFIX = "Router";
    /**
     * Proxy 后缀
     */
    public final static String PROXY_SUFFIX = "Proxy";

    /**
     * router缓存，key是className
     * 这个缓存只放有的，不放没有的
     */
    private Map<String, E6ProxyRouter> routerCache = new ConcurrentHashMap<>();
    /**
     * proxy缓存，key是className.methodName
     * 这个缓存放有和没有两种状态的
     */
    private Map<String, ObjAndMethod> proxyCache = new ConcurrentHashMap<>();

    /**
     * 代表没有代理类
     */
    final ObjAndMethod NullObjAndMethod = new ObjAndMethod(null,null);

    /**
     * 判断是否有配置Router和Proxy
     * 一个className，只能存在routerCache或者proxyCache中，不然就混乱了
     * 优先router，其次proxy
     * @param className
     * @param methodName
     * @return
     */
    @Override
    public boolean containsSignature(String className, String methodName, ProceedingJoinPoint joinPoint) {
        //step1:判断router缓存中有没有
        E6ProxyRouter routerInCache = routerCache.get(className);
        if(routerInCache!=null){
            return true;
        }
        //step2:判断proxy缓存中有没有
        ObjAndMethod proxyInCache = proxyCache.get(className + "." + methodName);
        if(proxyInCache!=null){
            //如果是NULL，则返回false,无代理  ;如果非NULL，则返回true 代表有代理
            return !NullObjAndMethod.equals(proxyInCache);
        }
        //step3: 两个缓存都没有，说明是初始化，从beanNames寻找className对应的router
        String[] classNames = className.split("\\.");
        String routerBeanName = classNames[classNames.length - 1] + ROUTER_SUFFIX;
        routerBeanName = first2LowerCase(routerBeanName);
        String proxyBeanName = classNames[classNames.length - 1] + PROXY_SUFFIX;
        proxyBeanName = first2LowerCase(proxyBeanName);
        //优先判断是否有router，如果没有则判断是否有proxy
        if(beanFactory.containsBean(routerBeanName)) {
            Object router = beanFactory.getBean(routerBeanName);
            if (!E6ProxyRouter.class.isInstance(router)) {
                return false;
            }
            E6ProxyRouter e6ProxyRouter = (E6ProxyRouter) router;
            //有router,放入缓存
            routerCache.put(className, e6ProxyRouter);
            //有router就可以返回true了，暂无必要判断到方法，到具体执行的时候判断即可
            return true;
        }else if(beanFactory.containsBean(proxyBeanName)){
            Object proxyObj = beanFactory.getBean(proxyBeanName);
            Method method = checkProxyMethod(proxyObj,className,methodName);
            //如果没有缓存，则存入一个NULL
            proxyCache.put(className + "." + methodName, method==null?NullObjAndMethod:(new ObjAndMethod(proxyObj, method)));
            return !Objects.isNull(method);
        }
        //到这一步，如果没有router也没有proxy，则proxy缓存中存入一个NullObjAndMethod
        proxyCache.put(className + "." + methodName,NullObjAndMethod);
        return false;
    }

    private Method checkProxyMethod(Object proxyObj,String className,String methodName){
        Method[] methods = proxyObj.getClass().getDeclaredMethods();
        Optional<Method> oneMethodOptional = Arrays.stream(methods).filter(method -> method.getName().equals(methodName)).findFirst();
        if (oneMethodOptional.isPresent()) {
            Method method = oneMethodOptional.get();
            logger.info("{}.{} 找到代理：{}", className, methodName, proxyObj);
            return method;
        }
        return null;
    }

    @Override
    public Object doMethod(String className, String methodName, ProceedingJoinPoint joinPoint) throws Throwable {
        String key = className + "." + methodName;
        //step1: 先判断有无router
        E6ProxyRouter router = routerCache.get(className);
        if(router!=null){
            Object proxyObject = router.getProxy(className,joinPoint);
            //没找到代理类，则穿透执行原方法
            if(proxyObject==null){
                return joinPoint.proceed();
            }
            Method method = checkProxyMethod(proxyObject,className,methodName);
            //没找到代理类中对应的方法，则穿透执行原方法
            if(method==null){
                return joinPoint.proceed();
            }
            logger.info("用代理执行[router]，old={},new={}", className + "." + methodName, proxyObject.getClass().getName());
            //执行代理
            return method.invoke(proxyObject, joinPoint.getArgs());
        }else if(proxyCache.containsKey(key)){
            ObjAndMethod objAndMethod = proxyCache.get(key);
            //如果没有，或者查出来是NULL，则说明无代理
            if (objAndMethod == null || NullObjAndMethod.equals(objAndMethod)) {
                return joinPoint.proceed();
            }
            Object proxyObject = objAndMethod.getObj();
            Method method = objAndMethod.getMethod();
            logger.info("用代理执行[proxy]，old={},new={}", className + "." + methodName, proxyObject.getClass().getName());
            //执行代理
            return method.invoke(proxyObject, joinPoint.getArgs());
        }
        return joinPoint.proceed();
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }


    /**
     * 首字母转小写
     * @param s
     * @return
     */
    public static String first2LowerCase(String s){
        return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    public class ObjAndMethod {
        Object obj;
        Method method;

        public Object getObj() {
            return obj;
        }

        public void setObj(Object obj) {
            this.obj = obj;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }

        public ObjAndMethod(Object obj, Method method) {
            this.obj = obj;
            this.method = method;
        }
    }
}
