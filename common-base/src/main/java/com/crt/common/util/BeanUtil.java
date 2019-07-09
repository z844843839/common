package com.crt.common.util;

import org.apache.commons.beanutils.BeanMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Bean工具
 */
public class BeanUtil {

    /**
     * 将Bean复制到Map里面去。
     *
     * @param bean
     * @param map
     */
    public static void bean2map(Object bean, Map map) {
        BeanMap beanMap = new BeanMap(bean);
        for (Object key : beanMap.keySet()) {
            Object value = beanMap.get(key);
            if (value == null) {
                continue;
            }
            map.put(key.toString(), value);
        }
        map.remove("class");
    }


    /**
     * 将首字母大写
     *
     * @param name
     * @return
     */
    public static String captureName(String name) {
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;

    }

    /**
     * 复制sour里属性不为空的值到obje为空的属性
     *
     * @param obje    目标实体类
     * @param sour    源实体类
     * @param isCover 是否保留obje类里不为null的属性值(true为保留源值，属性为null则赋值)
     * @return obje
     */
    public static Object Copy(Object obje, Object sour, boolean isCover) {
        Field[] fields = sour.getClass().getDeclaredFields();
        for (int i = 0, j = fields.length; i < j; i++) {
            String propertyName = fields[i].getName();
            Object propertyValue = getProperty(sour, propertyName);
            if (isCover) {
                if (getProperty(obje, propertyName) == null && propertyValue != null) {
                    Object setProperty = setProperty(obje, propertyName, propertyValue);
                }
            } else {
                Object setProperty = setProperty(obje, propertyName, propertyValue);
            }

        }
        return obje;
    }

    /**
     * 复制sour里属性不为空的值到obj里并相加
     *
     * @param obj     目标实体类
     * @param sour    源实体类
     * @param isCover
     * @return obj
     */
    public static Object CopyAndAdd(Object obj, Object sour, boolean isCover) {
        Field[] fields = sour.getClass().getDeclaredFields();
        for (int i = 0, j = fields.length; i < j; i++) {
            String propertyName = fields[i].getName();
            Object sourPropertyValue = getProperty(sour, propertyName);
            Object objPropertyValue = getProperty(obj, propertyName);
            if (isCover) {
                if (objPropertyValue == null && sourPropertyValue != null) {
                    Object setProperty = setProperty(obj, propertyName, sourPropertyValue);
                } else if (objPropertyValue != null && sourPropertyValue == null) {
                    Object setProperty = setProperty(obj, propertyName, objPropertyValue);
                } else if (objPropertyValue != null && sourPropertyValue != null) {
                    Object setProperty = setProperty(obj, propertyName, ((int) sourPropertyValue) + (int) objPropertyValue);
                }
            }

        }
        return obj;
    }


    /**
     * 得到值
     *
     * @param bean
     * @param propertyName
     * @return
     */
    private static Object getProperty(Object bean, String propertyName) {
        Class clazz = bean.getClass();
        try {
            Field field = clazz.getDeclaredField(propertyName);
            Method method = clazz.getDeclaredMethod(getGetterName(field.getName()), new Class[]{});
            return method.invoke(bean, new Object[]{});
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 给bean赋值
     *
     * @param bean
     * @param propertyName
     * @param value
     * @return
     */
    private static Object setProperty(Object bean, String propertyName, Object value) {
        Class clazz = bean.getClass();
        try {
            Field field = clazz.getDeclaredField(propertyName);
            Method method = clazz.getDeclaredMethod(getSetterName(field.getName()), new Class[]{field.getType()});
            return method.invoke(bean, new Object[]{value});
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 根据变量名得到get方法
     *
     * @param propertyName
     * @return
     */
    private static String getGetterName(String propertyName) {
        String method = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        return method;
    }

    /**
     * 得到setter方法
     *
     * @param propertyName 变量名
     * @return
     */
    private static String getSetterName(String propertyName) {
        String method = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        return method;
    }

    /**
     * 父类集合转成子类集合集合通用方法(子类集合接收父类集合)
     *
     * @param list 父类集合
     * @param <T>  子类
     * @param <E>  父类
     * @return
     */
    public static <T, E> List<T> chang2ChildClassList(List<E> list) {
        List<T> alist = new ArrayList<>();
        for (E o : list) {
            alist.add((T) o);
        }
        return alist;

    }
}
