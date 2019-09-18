package com.crt.common.util;

import com.crt.common.constant.Constants;
import com.crt.common.vo.RowAuthVO;
import org.apache.commons.beanutils.BeanMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.hutool.core.bean.BeanUtil.mapToBean;

/**
 * Bean工具
 * @author malin
 */
public class BeanUtil {

    private static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);

    /**
     * 实体对象转为Map
     * @param entity
     * @return Map
     */
    public static <T> Map<String,Object> objectToMap(T entity){
        Map<String, Object> map = new HashMap<>(Constants.HASHMAP_DEFAULT_SIZE);
        if (null == entity) {
            return null;
        }
        Field[] fields = entity.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), getProperty(entity,field.getName()));
            }
        } catch (Exception e) {
            logger.error("[objectToMap] exception message : " + e.getMessage());
        }
        return map;
    }

    /**
     * map转为实体对象
     * @param map
     * @param clazz
     * @param <T>
     * @return entity
     */
    public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
        T entity = null;
        if (null == map){
            return entity;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            // 使用newInstance来创建对象
            entity = clazz.newInstance();
            // 获取类中的所有字段
            Field[] fields = entity.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                // 判断是拥有某个修饰符
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                // 当字段使用private修饰时，需要加上
                field.setAccessible(true);
                // 获取参数类型名字
                String filedTypeName = field.getType().getName();
                // 判断是否为时间类型，使用equalsIgnoreCase比较字符串，不区分大小写
                // 给 entity 的属性赋值
                if (filedTypeName.equalsIgnoreCase("java.util.date")) {
                    String datetimestamp = (String) map.get(field.getName());
                    if (datetimestamp.equalsIgnoreCase("null")) {
                        field.set(entity, null);
                    } else {
                        field.set(entity, sdf.parse(datetimestamp));
                    }
                }else {
                    field.set(entity, map.get(field.getName()));
                }
            }
        }catch (Exception e){
            logger.error("[mapToObject] exception message : " + e.getMessage());
        }
        return entity;
    }

    /**
     * listMap 转 listBean
     * @param data
     * @param clazz
     * @param <T>
     * @return List<T>
     */
    public static <T> List<T> listMapToListBean(List<Map<String, Object>> data, Class<T> clazz){
        List<T> result = null;
        if (null == data){
            return result;
        }
        result = new ArrayList<>();
        for (Map<String,Object> map : data) {
            T entity =  mapToBean(map,clazz,false);
            result.add(entity);
        }
        return result;
    }

    /**
     * listBean 转 listMap
     * @param data
     * @param <T>
     * @return
     */
    public static <T> List<Map<String,Object>> listBeanToListMap(List<T> data){
        List<Map<String,Object>> result = null;
        if (null == data){
            return result;
        }
        result = new ArrayList<>();
        for (T entity : data){
            Map<String,Object> map = objectToMap(entity);
            result.add(map);
        }
        return result;
    }

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

    public static void main(String[] args) {
        List<RowAuthVO> ravs = new ArrayList<>();
        RowAuthVO rav = new RowAuthVO();
        rav.setOrgRoleCode(1L);
        rav.setColumnType("varchar");
        rav.setSetColumn("userName");
        rav.setSetOperator("LIKE");
        rav.setSetTable("user_user");
        rav.setSetValue("王");
        ravs.add(rav);
        List<Map<String,Object>> map =  listBeanToListMap(ravs);
        System.out.println(map.toString());
        List<Map<String,Object>> maps = new ArrayList<>();
        Map<String,Object> mm = new HashMap<>();
        mm.put("columnType","varchar");
        mm.put("orgRoleCode",2L);
        mm.put("setColumn","userName");
        mm.put("setTable","user_user");
        mm.put("setValue","王");
        mm.put("setOperator","LIKE");
        maps.add(mm);
        List<RowAuthVO> rv = listMapToListBean(maps,RowAuthVO.class);
        System.out.println(rv.toString());

    }
}
