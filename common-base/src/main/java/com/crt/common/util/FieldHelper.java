package com.crt.common.util;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/***
 * 获取属性与对应的中文名工具类
 * @author malin
 * @date 2020-06-30
 * @version 1.0
 */
public class FieldHelper {

    /**
     * 根据实体类名获取字段名称和中文名称
     * @param clzz 实体类
     * @return List<Map<String,String>>
     */
    public static List<LinkedHashMap<String,String>> initAnnoFieldDic(@SuppressWarnings("rawtypes") Class clzz){
        //用于存储字段和中文值的集合
        List<LinkedHashMap<String,String>> fieldList = new ArrayList<>();
        //用于存储实体类字段(key)和中文名(value)
        LinkedHashMap<String,String> valueMap = new LinkedHashMap<>();
        //获取对象中所有的Field
        Field[] fields = clzz.getDeclaredFields();
        //循环实体类字段集合,获取标注@ColumnConfig的字段
        for (Field field : fields) {
            if(field.isAnnotationPresent(ColumnConfig.class)){
                //获取字段名
                String fieldNames = field.getName();
                //获取字段注解
                ColumnConfig columnConfig = field.getAnnotation(ColumnConfig.class);
                //判断是否已经获取过该code的字典数据 避免重复获取
                if(null == valueMap.get(columnConfig.description())){
                    valueMap.put(fieldNames, columnConfig.description());
                }
            }
        }
        fieldList.add(valueMap);//将LinkedHashMap放入List集合中
        return fieldList;
    }
}
