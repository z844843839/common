package com.crt.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @Description spring工具类
 * @Author  王龙龙
 * @Created Date: 2019/10/21 16:16
*/
@Component
public class SpringUtil implements ApplicationContextAware {
    /**
     * 说明：声明静态变量log
     */
    static Logger log = LoggerFactory.getLogger(SpringUtil.class);
    /**
     * 说明：定义一个只能在该类使用的静态变量applicationContext
     */
    private static ApplicationContext applicationContext;

    /**
     * (non-Javadoc)
     * @Title: setApplicationContext
     * @See: 实现ApplicationContextAware接口的回调方法，设置上下文环境
     * @param applicationContext 应用前后关系
     * @throws BeansException
     * @date: 2015年4月28日 上午11:57:28
     */
    public final void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }

    /**
     * 说明：通过Spring配置文件中定义的bean名称，从IOC容器中取得实例
     * @Title: getBean
     * @See:
     * @param beanName Bean名
     * @return Bean名称对应实例Object，使用时需要强制类型转换
     * @date: 2015年4月28日 下午12:00:49
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        try {
            T bean = (T) applicationContext.getBean(beanName);
            return bean;
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("Spring配置文件中定义的bean名称不存在,错误信息:", e.getMessage());
            }
        }
        return null;
    }

    //通过class获取Bean.
    public static <T> T getBean(Class<T> clazz){
        return applicationContext.getBean(clazz);
    }

    /**
     * 说明：通过Spring配置文件中定义的bean名称，从IOC容器中取得bean类型
     * @Title: getType
     * @See:
     * @param beanName Bean名
     * @return Bean名称对应bean类型
     * @date: 2015年12月18日上午09:40:49
     */
    public static Class<?> getType(String beanName) {
        try {
            Class<?> beanType = applicationContext.getType(beanName);
            return beanType;
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("Spring配置文件中定义的bean名称不存在,错误信息:", e.getMessage());
            }
        }
        return null;
    }

    /**
     * 说明：获取指定类型的Bean
     * @Title: getBeansByType
     * @See:
     * @param beanType Bean类名
     * @return 指定类型的Bean
     * @date: 2015年4月28日 下午12:55:31
     */
    public static Map<String, ?> getBeansByType(@SuppressWarnings("rawtypes") Class beanType) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, ?> beans = applicationContext.getBeansOfType(beanType);
            return beans;
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("获取指定类型的Bean不存在,错误信息:", e.getMessage());
            }
        }
        return null;
    }

    /**
     * 说明：获取指定类型的Bean映射到MAP
     * @Title: getBeanByType
     * @See:
     * @param beanType bean类
     * @return 映射结果
     * @date: 2015年4月28日 下午1:02:16
     */
    public static <T> T getBeanByType(Class<T> beanType) {
        try {
            Map<String, T> beans = SpringUtil.applicationContext.getBeansOfType(beanType);
            if (beans != null && beans.size() > 0) {
                return beans.values().iterator().next();
            }
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("获取指定类型的Bean映射到MAP不存在,错误信息:", e.getMessage());
            }
        }
        return null;
    }

    /**
     *
     * 说明： 代理处理方法
     * @Title: proxyMethodHandle
     * @See: {@link #proxyMethodHandle(Object, Object[], String, Class...)}
     * @param proxy 代理实例对象
     * @param params 设置的参数
     * @param methodName 方法名
     * @param parameterTypes 方法类型
     * @return
     * @date: 2015年10月16日 下午4:51:58
     */
    public static <T> T proxyMethodHandle(T proxy, Object[] params, String methodName, Class<?>... parameterTypes) {
        try {
            // 取得代理类处理器
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(proxy);
            // 取得需要反射的方法
            Method method = AopProxyUtils.ultimateTargetClass(proxy).getMethod(methodName, parameterTypes);
            // 设置模板路径
            invocationHandler.invoke(proxy, method, params);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return proxy;
    }
}
