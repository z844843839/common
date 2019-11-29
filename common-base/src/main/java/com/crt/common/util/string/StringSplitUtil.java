package com.crt.common.util.string;

import java.util.ArrayList;

/**
 * 字符串Split方法
 * 应用场景：
 * 1. 总线轨迹压缩包中Json字符串拆字符串数组
 * 2. 从字符串中截取车辆ID
 * 经测试效率比正则表达式和fastjosn效率高，
 * Created by wanggaoli on 2017/8/25.
 */
public class StringSplitUtil {
    /**
     * String vehicleId = StringSplitUtil.getDesiredStr(val,"VehicleID\":", ",");
     * @param str 需查找字符串
     * @param start 开始字符串
     * @param end 结束字符串
     * @return 截取内容
     */
    public static String getDesiredStr(String str, String start, String end){
        int offset = start.length();
        int sindex = str.indexOf(start);
        if(-1 == sindex){
            return null;
        }
        int eindex = str.indexOf(end, sindex+offset);
        if(-1 == eindex){
            return null;
        }
        //FIXME: for jdk version < 1.7
        return str.substring(sindex+offset, eindex);
    }


    /**
     * 从字符串中截取字符数组
     * StringSplitUtil.getStrArray(unCompressStr, "}\",",2,1,1);
     * @param str
     * @param delim
     * @param addLen
     * @param firstLen
     * @param lastLen
     * @return
     */
    public static ArrayList<String> getStrArray(String str, String delim, int addLen, int firstLen, int lastLen){
        ArrayList<String> list = new ArrayList<>();

        int strLen = str.length();
        int delimLen = delim.length();
        if(firstLen + lastLen >= strLen){
            return list;
        }
        int lastPos = strLen - lastLen;
        int index = str.indexOf(delim);
        if(-1 == index){
            list.add(str.substring(firstLen, lastPos));
            return list;
        }
        int pos = 0;
        if(firstLen > 0){
            pos += firstLen;
        }
        while(index != -1){
            list.add(str.substring(pos, index+addLen));
            pos = index + delimLen;
            index = str.indexOf(delim, pos);
        }

        if(pos < lastPos){
            list.add(str.substring(pos, lastPos));
        }
        return list;
    }
}
