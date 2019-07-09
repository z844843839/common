package com.crt.common.util;

import com.crt.common.vo.E6Wrapper;
import com.crt.common.vo.E6WrapperUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description 参数校验工具
 * @Author changyandong@e6yun.com
 * @Emoji (゜ - ゜)つ干杯
 * @Created Date: 2019/6/14 10:55
 * @ClassName CheckParamUtils
 * @Version: 1.0
 */
public class CheckParamUtils {
    /**
     * 校验部分属性非空
     * @param logger
     * @param bean
     * @param fieldNames
     * @param <T>
     * @return
     */
    public static <T> E6Wrapper checkParamNotEmpty(Logger logger,T bean,String...fieldNames){
        Field[] declaredFields = getFieldArrayByFieldName(logger, bean, fieldNames);
        return checkParam(logger, bean, declaredFields);
    }

    /**
     * 校验全部属性非空
     * @param logger
     * @param bean
     * @param <T>
     * @return
     */
    public static  <T> E6Wrapper checkAllParamNotEmpty(Logger logger, T bean){
        return checkParam(logger, bean, bean.getClass().getDeclaredFields());
    }

    /**
     * 通过fieldName 获取 Field
     * @param logger
     * @param bean
     * @param fieldNames
     * @param <T>
     * @return
     */
    public static <T> Field[] getFieldArrayByFieldName(Logger logger, T bean, String...fieldNames) {
        Class<?> clazz = bean.getClass();
        Set<Field> fieldSet = new HashSet<>();
        for(String fieldName : fieldNames){
            Field declaredField = null;
            try {
                declaredField = clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                logger.info("未获取到 {} 的 {} 属性",clazz.getName(),fieldName);
            }
            if(declaredField !=null){
                fieldSet.add(declaredField);
            }
        }
        return fieldSet.toArray(new Field[fieldSet.size()]);
    }


    public static <T> E6Wrapper checkParam(Logger logger, T bean, Field...declaredFields) {
        for(Field field : declaredFields){
            field.setAccessible(true);
            Object value = null;
            try {
                value = field.get(bean);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if(value == null){
                return parameterVerificationFailed(logger, field);
            }

            if(value instanceof String){
                String valueStr = (String)value;
                if(StringUtils.isEmpty(valueStr)){
                    return parameterVerificationFailed(logger, field);
                }
                continue;
            }
            if(value instanceof Collection){
                Collection valueCollection = (Collection) value;
                if(CollectionUtils.isEmpty(valueCollection)){
                    return parameterVerificationFailed(logger,field);
                }
                continue;
            }
        }
        return null;
    }

    private static E6Wrapper parameterVerificationFailed(Logger logger, Field field) {
        logger.info("参数:{} == null",field.getName());
        return E6WrapperUtil.error("参数:"+field.getName()+"=null");
    }
}
