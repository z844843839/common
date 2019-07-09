package com.crt.common.util;

import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: caolu@e6yun.com
 * @Date: 2019/4/23 21:09
 * @Description: 处理fastjson反序列化Map的问题
 */
public class E6MapJSONParser<R,T> {

    private static final String SETTER_PREFIX = "set";

    /**
     * 处理Map<R,Object>
     * @param jsonStr
     * @param tClazz
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public Map<R,T> parseForObject(String jsonStr, Class tClazz) throws IllegalAccessException, InstantiationException, InvocationTargetException {

        Map<R, JSONObject> mapObj= (Map<R, JSONObject>) JSONObject.parse(jsonStr);
        Map<R, T> result=new HashMap<>();
        for(R key:mapObj.keySet()) {
            Object tInstance= tClazz.newInstance();
            Method[] methods = tClazz.getDeclaredMethods();
            JSONObject value=mapObj.get(key);
            for(Method method:methods){
                String methodName=method.getName();
                if(methodName.contains(SETTER_PREFIX)){
                    String filedName=methodName.replace(SETTER_PREFIX, "");
                    Object filedValue=value.get(filedName);
                    method.invoke(tInstance,filedValue);
                }
            }
            result.put((R)key,(T)tInstance);
        }
        return result;
    }


}
