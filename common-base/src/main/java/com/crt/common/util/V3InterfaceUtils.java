package com.crt.common.util;

import com.crt.common.util.date.DateStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Description V3接口签名工具类
 * @Author xiashuhao@e6yun.com
 * @Created Date: 2019/5/30 16:44
 * @ClassName V3InterfaceUtils
 * @Version: 1.0
 */
public class V3InterfaceUtils {

    private static final Logger logger = LoggerFactory.getLogger(V3InterfaceUtils.class);

    /**
     * 获取签名
     *
     * @param appKey
     * @param appSecret
     * @param regName
     * @return String
     */
    private static String getSign(String appKey, String appSecret, String regName, String nowTime, String btime, String etime, Integer validDay) {
        Map<String, String> signMap = new LinkedHashMap<>();
        // 生成签名 先将key进行排序，然后再根据key值在map当中获取相应的值，然后再进行添加
        signMap.put("appKey", appKey);
        signMap.put("timestamp", nowTime);
        signMap.put("vehicle", regName);
        signMap.put("btime", btime);
        signMap.put("etime", etime);
        signMap.put("effectivelength", String.valueOf(validDay * 24 * 60));

        //将所有的键放在一个collection进行排序，是为了生成签名用到
        ArrayList<String> keyList = new ArrayList<>(signMap.keySet());

        //对键进行排序
        Collections.sort(keyList);

        //根据键再从map当中获取值，拼接成字符串
        StringBuilder sb = new StringBuilder();
        for (String key : keyList) {
            String value = signMap.get(key);
            if (value != null) {
                sb.append(key);
                sb.append(value);
            }

        }
        String s = sb.toString();
        s = appSecret + s + appSecret;
        String md5Str = Md5Util.str2MD5(s);
        return md5Str.toUpperCase();
    }

    /**
     * 获取签名url
     *
     * @param appKey
     * @param appSecret
     * @param regName
     * @param beginTime
     * @param endTime
     * @param validDay
     * @return String
     */
    public static String getUrl(String appKey, String appSecret, String regName, Date beginTime, Date endTime, Integer validDay) {
        String nowTime = DateUtils.formatLocalDateTimeToString(LocalDateTime.now(), DateStyle.YYYY_MM_DD_HH_MM_SS);
        String bTime = DateUtils.formatLocalDateTimeToString(DateUtils.date2LocalDateTime(beginTime), DateStyle.YYYY_MM_DD_HH_MM_SS);
        String eTime = DateUtils.formatLocalDateTimeToString(DateUtils.date2LocalDateTime(endTime), DateStyle.YYYY_MM_DD_HH_MM_SS);
        String sign = getSign(appKey, appSecret, regName, nowTime, bTime, eTime, validDay);

        StringBuilder url = new StringBuilder();
        url.append("appKey=").append(appKey);
        url.append("&timestamp=").append(nowTime);
        try {
            url.append("&vehicle=").append(URLEncoder.encode(regName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error("url解码出现异常", e);
        }
        url.append("&btime=").append(bTime);
        url.append("&etime=").append(eTime);
        url.append("&effectivelength=").append(validDay * 24 * 60);
        url.append("&sign=").append(sign);
        return String.valueOf(url);
    }


}
