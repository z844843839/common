package com.crt.common.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @description: 为了用切面给保存和修改的时候补全createUser信息和modifiedUser信息通过反射统一处理
 * 暂未找到其他工具类有setFieldValue方法
 * @author: wangshuai@e6yun.com
 * @date: 2018/08/15
 */
public class ClassRefUtil {

    static Logger logger = LoggerFactory.getLogger(ClassRefUtil.class);
    /**
     * 取Bean的属性和值对应关系的MAP
     *
     * @param bean
     * @return Map
     */
    public static Object getFieldValue(Object bean, String fieldName) {
        Class<?> cls = bean.getClass();
        try {
            String fieldGetName = parGetName(fieldName);
            Method fieldGetMet = cls.getMethod(fieldGetName, new Class[]{});
            Object fieldVal = fieldGetMet.invoke(bean, new Object[]{});
            return fieldVal;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * set属性的值到Bean
     *
     * @param bean
     * @param valMap
     */
    public static void setFieldValue(Object bean, Map<String, Object> valMap) {
        Class<?> cls = bean.getClass();
        valMap.forEach((key, value) -> {
            try {
                Field field = cls.getDeclaredField(key);
                String fieldSetName = parSetName(field.getName());
                Method fieldSetMetMethod = cls.getMethod(fieldSetName, field.getType());
                fieldSetMetMethod.invoke(bean, value);
            } catch (Exception e) {
                logger.error("发生异常 {}",e);
            }
        });
    }

    /**
     * 拼接某属性的 get方法
     *
     * @param fieldName
     * @return String
     */
    private static String parGetName(String fieldName) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        return "get" + fieldName.substring(0, 1).toUpperCase()
                + fieldName.substring(1);
    }

    /**
     * 拼接在某属性的 set方法
     *
     * @param fieldName
     * @return String
     */
    private static String parSetName(String fieldName) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        return "set" + fieldName.substring(0, 1).toUpperCase()
                + fieldName.substring(1);
    }
}