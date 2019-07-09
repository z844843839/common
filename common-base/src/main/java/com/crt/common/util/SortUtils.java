package com.crt.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Description
 * @Author changyandong@e6yun.com
 * @Created Date: 2019/1/10 17:23
 * @ClassName SortUtils
 * @Version: 1.0
 */
public class SortUtils {
    static Logger logger = LoggerFactory.getLogger(SortUtils.class);
    private static final String  GET_MEHTOD_PREIX = "get";

    public static <T> void sortAsc(Class<T> clazz, List<T> list, String propName){
        propName = toUpperCaseFirstOne(propName);
        String methodName = GET_MEHTOD_PREIX+propName;
        try {
            Method method = clazz.getMethod(methodName);
            list.sort((bean1,bean2)->{
                try {
                    Object bean1InvokeResult = method.invoke(bean1);
                    Object bean2InvokeResult = method.invoke(bean2);
                    if(bean1InvokeResult == null || bean2InvokeResult == null){
                        return -1;
                    }
                    if(bean1InvokeResult instanceof Integer){
                        Integer bean1NumberVal = (Integer) bean1InvokeResult;
                        Integer bean2NumberVal = (Integer) bean2InvokeResult;
                        return bean1NumberVal.compareTo(bean2NumberVal);
                    }
                    if(bean1InvokeResult instanceof String){
                        String bean1NumberVal = (String) bean1InvokeResult;
                        String bean2NumberVal = (String) bean2InvokeResult;
                        return bean1NumberVal.compareTo(bean2NumberVal);
                    }

                } catch (IllegalAccessException e) {
                    logger.error("sort工具内部发生异常",e);
                } catch (InvocationTargetException e) {
                    logger.error("sort工具内部发生异常",e);
                }
                return -1;
            });
        } catch (NoSuchMethodException e) {
            logger.error("sort工具内部发生异常",e);
        }
    }

    public static <T> void sortDesc(Class<T> clazz,List<T> list,String propName){
        propName = toUpperCaseFirstOne(propName);
        String methodName = GET_MEHTOD_PREIX+propName;
        try {
            Method method = clazz.getMethod(methodName);
            list.sort((bean1,bean2)->{
                try {
                    Object bean1InvokeResult = method.invoke(bean1);
                    Object bean2InvokeResult = method.invoke(bean2);
                    if(bean1InvokeResult == null || bean2InvokeResult == null){
                        return -1;
                    }
                    if(bean1InvokeResult instanceof Integer){
                        Integer bean1NumberVal = (Integer) bean1InvokeResult;
                        Integer bean2NumberVal = (Integer) bean2InvokeResult;
                        return bean2NumberVal.compareTo(bean1NumberVal);
                    }
                    if(bean1InvokeResult instanceof String){
                        String bean1NumberVal = (String) bean1InvokeResult;
                        String bean2NumberVal = (String) bean2InvokeResult;
                        return bean2NumberVal.compareTo(bean1NumberVal);
                    }

                } catch (IllegalAccessException e) {
                    logger.error("sort工具内部发生异常",e);
                } catch (InvocationTargetException e) {
                    logger.error("sort工具内部发生异常",e);
                }
                return -1;
            });
        } catch (NoSuchMethodException e) {
            logger.error("sort工具内部发生异常",e);
        }
    }


    private static String toUpperCaseFirstOne(String s) {
        if(StringUtils.isEmpty(s)){
            return "";
        }
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        }
        return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }
}
