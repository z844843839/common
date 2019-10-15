package com.crt.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.crt.middle.rpc.KeyValueStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description 缓存工具
 * @Author 王龙龙
 * @Created Date: 2019/10/14 10:55
 * @Version: 1.0
 */
@Component
public class CacheUtil {

    private static  KeyValueStorage keyValueStorage;

    @Autowired
    private   KeyValueStorage cache;

    @PostConstruct
    public void beforeInit() {
        keyValueStorage = cache;

    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public static Boolean setExpireTime(String key, long time) {
        try {
            if (time > 0) {
                keyValueStorage.setExpireTime(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public static Boolean hasKey(String key) {
        try {
            return keyValueStorage.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public static Boolean delete(String key) {
        try {
            return keyValueStorage.delete(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public static String getStr(String key) {
        return key == null ? null : keyValueStorage.get(key);
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @param c
     * @return 值
     */
    public static <T> T  get(String key, Class<?> c) {
        return key == null ? null : JSONObject.parseObject(keyValueStorage.get(key), (Type) c);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public static Boolean set(String key, Object value) {
        try {
            keyValueStorage.set(key, JSON.toJSONString(value));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public static Boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                keyValueStorage.setValAndTime(key, JSON.toJSONString(value), time);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public static Long increase(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return keyValueStorage.increment(key, delta);
    }

    /**
     * 生成业务递增编码
     * @param busCode
     * @return
     */
    public static Long businessCode(int busCode){
        //根据业务规则，获取当天六位数
        String dateStr = new SimpleDateFormat("YYMMdd")
                .format(new Date());
        //业务编码 + 6位日期 作为Key
        String key = busCode + dateStr;
        //生成6位自增数
        Long autoNum = increase(key,1);
        //不足6位，前面补0
        String autoNumStr = String.format("%0" + 6 + "d", autoNum);
        Long code = Long.parseLong(key + autoNumStr);
        //设置编码保存时长
        setExpireTime(key,24 * 60 * 60);
        return code;
    }

    /**
     *根据传入的页面编码前缀和自增字符长度返回业务编码
     * cp11+190929+000001
     * redis保存1天
     * @param busCode 需要生成的业务前缀编码例如：210，cp11 保证每个编码全项目唯一
     * @param strLength 要返回的业务后面自增的字符长度 大于1的整数
     * @return 例如传入的busCode：cp11 strLength：6  则返回业务编码cp11190928000001
     */
    public static String createBusinessCode(String busCode,int strLength){
        //根据业务规则，获取当天六位数
        String dateStr = new SimpleDateFormat("YYMMdd")
                .format(new Date());
        //业务编码 + 6位日期 作为Key
        String key = busCode + dateStr;
        //生成6位自增数
        Long autoNum = increase(key,1);
        //不足6位，前面补0
        String autoNumStr = String.format("%0" + strLength + "d", autoNum);
        String businessCode =key + autoNumStr;
        //设置编码保存时长
        setExpireTime(key,24 * 60 * 60);
        return businessCode;
    }

    /**
     * 根据key生成自增指定长度的字符串
     * @param key
     * @param strLength 返回长度以0补齐
     * @return
     */
    public static String increaseStr(String key,int strLength){
        //生成strLength位自增数
        Long autoNum = increase(key,1);
        //不足strLength位，前面补0
        String autoNumStr = String.format("%0" + strLength + "d", autoNum);
        //设置编码保存时长
        setExpireTime(key,365*2*24 * 60 * 60);
        return autoNumStr;
    }


    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public static Long decrease(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return keyValueStorage.increment(key, -delta);
    }

}
