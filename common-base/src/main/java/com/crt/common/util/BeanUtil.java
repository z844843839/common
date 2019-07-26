package com.crt.common.util;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
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
     * 源对象不为空的值替换目标对象的值
     *
     * @param source 源对象
     * @param target 目标对象
     * @return 最新源对象
     */
    public static Object cover(Object source, Object target) {
        Class<?> srcClass = source.getClass();
        Field[] fields = srcClass.getDeclaredFields();
        for (Field field : fields) {
            String nameKey = field.getName();
            //获取源对象中的属性值
            String srcValue = getClassValue(source, nameKey) == null ? "" : getClassValue(source, nameKey).toString();
            //获取目标对象中的属性值
            String tarValue = getClassValue(target, nameKey) == null ? "" : getClassValue(target, nameKey).toString();
            //当目标对象中的值不等于空的时候，比较两个属性值，不相等的时候进行赋值
            if (StringUtils.isNotEmpty(tarValue) && !srcValue.equals(tarValue)) {
                source = setClassValue(source, nameKey, tarValue);
            }
        }
        return source;
    }

    /**
     * 根据字段名称取值
     *
     * @param obj       指定对象
     * @param fieldName 字段名称
     * @return 指定对象
     */
    private static Object getClassValue(Object obj, String fieldName) {
        if (obj == null) {
            return null;
        }
        Class beanClass = obj.getClass();
        Method[] ms = beanClass.getMethods();
        for (int i = 0; i < ms.length; i++) {
            // 非get方法不取
            if (!ms[i].getName().startsWith("get")) {
                continue;
            }
            Object objValue = null;
            try {
                objValue = ms[i].invoke(obj, new Object[]{});
            } catch (Exception e) {
                continue;
            }
            if (objValue == null) {
                continue;
            }
            if (ms[i].getName().toUpperCase().equals(fieldName.toUpperCase())
                    || ms[i].getName().substring(3).toUpperCase().equals(fieldName.toUpperCase())) {
                return objValue;
            }
        }
        return null;
    }

    /**
     * 给对象的字段赋指定的值
     *
     * @param obj       指定对象
     * @param fieldName 属性
     * @param value     值
     * @return
     * @see
     */
    private static Object setClassValue(Object obj, String fieldName, Object value) {
        if (obj == null) {
            return null;
        }
        if ("null".equals(value)) {
            value = null;
        }
        Class beanClass = obj.getClass();
        Method[] ms = beanClass.getMethods();
        for (int i = 0; i < ms.length; i++) {
            try {
                if (ms[i].getName().startsWith("set")) {
                    if (ms[i].getName().toUpperCase().equals(fieldName.toUpperCase())
                            || ms[i].getName().substring(3).toUpperCase().equals(fieldName.toUpperCase())) {
                        String pt = ms[i].getParameterTypes()[0].toString();
                        if (value != null) {
                            ms[i].invoke(obj, transVal(value.toString(), pt.substring(pt.lastIndexOf(".") + 1)));
                        } else {
                            ms[i].invoke(obj, new Object[]{null});
                        }
                        break;
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }
        return obj;
    }

    /**
     * 根据属性类型赋值
     *
     * @param value      值
     * @param paramsType 属性类型
     * @return
     * @see
     */
    private static Object transVal(String value, String paramsType) {
        if (ColumnType.String.toString().equals(paramsType)) {
            return new String(value);
        }
        if (ColumnType.Double.toString().equals(paramsType)) {
            return Double.parseDouble(value);
        }
        if (ColumnType.Integer.toString().equals(paramsType)) {
            return Integer.parseInt(value);
        }
        if (ColumnType.Long.toString().equals(paramsType)) {
            return Long.parseLong(value);
        }
        if (ColumnType.BigDecimal.toString().equals(paramsType)) {
            return new BigDecimal(value);
        }
        return value;
    }

    /**
     * <简述> 定义类型枚举
     * <详细描述>
     *
     * @author malin
     */
    private enum ColumnType {
        String, Double, Integer, Long, BigDecimal;
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
    public static Object getProperty(Object bean, String propertyName) {
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
