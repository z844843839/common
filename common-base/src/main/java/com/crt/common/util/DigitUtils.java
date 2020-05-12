package com.crt.common.util;

import com.crt.common.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * 数字转大写
 * @author wangxin@e6yun.com
 */
public class DigitUtils {

    /***日志***/
    private static final Logger logger = LoggerFactory.getLogger(DigitUtils.class);

    /**
     * 数字转大写
     * @param n 数字
     * @return 大写
     */
    public static String digitUppercase(double n){
        String[] fraction = {"角", "分"};
        String[] digit = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
        String[][] unit = {{"元", "万", "亿"},
                {"", "拾", "佰", "仟"}};

        String head = n < 0? "负": "";
        n = Math.abs(n);

        String s = "";
        for (int i = 0; i < fraction.length; i++) {
            s += (digit[(int)(Math.floor(n * 10 * Math.pow(10, i)) % 10)] + fraction[i]).replaceAll("(零.)+", "");
        }
        if(s.length()<1){
            s = "整";
        }
        int integerPart = (int)Math.floor(n);

        for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
            String p ="";
            for (int j = 0; j < unit[1].length && n > 0; j++) {
                p = digit[integerPart%10]+unit[1][j] + p;
                integerPart = integerPart/10;
            }
            s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i] + s;
        }
        return head + s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "").replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
    }

    /**
     * 两个数相加
     * @param str1
     * @param str2
     * @param keepType 结果保留方式（1 四舍五入 2 四舍六入五成双 3 直接舍去保留意外的数字）
     * @param several 保留小数位
     * @return
     */
    public static String add(String str1,String str2,int keepType,int several) {
        try {
            String result = new BigDecimal(str1).add(new BigDecimal(str2)).toPlainString();
            if (Constants.NUMBER_TWO == keepType){
                result = fourUpSixInto(result,several);
            }else if (Constants.NUMBER_THREE == keepType){
                result = into(result,several);
            }else {
                result = fourUpFiveInto(result,several);
            }
            return result;
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 两个数相乘
     * @param str1
     * @param str2
     * @param keepType 结果保留方式（1 四舍五入 2 四舍六入五成双 3 直接舍去保留意外的数字）
     * @param several 保留小数位
     * @return
     */
    public static String multiply(String str1,String str2,int keepType,int several) {
        try {
            String result = new BigDecimal(str1).multiply(new BigDecimal(str2)).toPlainString();
            if (Constants.NUMBER_TWO == keepType){
                result = fourUpSixInto(result,several);
            }else if (Constants.NUMBER_THREE == keepType){
                result = into(result,several);
            }else {
                result = fourUpFiveInto(result,several);
            }
            return result;
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 两个数相减
     * @param str1
     * @param str2
     * @param keepType 结果保留方式（1 四舍五入 2 四舍六入五成双 3 直接舍去保留意外的数字）
     * @param several 保留小数位
     * @return
     */
    public static String subtract(String str1,String str2,int keepType,int several) {
        try {
            String result = new BigDecimal(str1).subtract(new BigDecimal(str2)).toPlainString();
            if (Constants.NUMBER_TWO == keepType){
                result = fourUpSixInto(result,several);
            }else if (Constants.NUMBER_THREE == keepType){
                result = into(result,several);
            }else {
                result = fourUpFiveInto(result,several);
            }
            return result;
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 两个数相除
     * @param str1
     * @param str2
     * @param keepType 结果保留方式（1 四舍五入 2 四舍六入五成双 3 直接舍去保留意外的数字）
     * @param several 保留小数位
     * @return
     */
    public static String divide(String str1,String str2,int keepType,int several) {
        try {
            String result = new BigDecimal(str1).divide(new BigDecimal(str2)).toPlainString();
            if (Constants.NUMBER_TWO == keepType){
                result = fourUpSixInto(result,several);
            }else if (Constants.NUMBER_THREE == keepType){
                result = into(result,several);
            }else {
                result = fourUpFiveInto(result,several);
            }
            return result;
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 四舍五入
     * @param str
     * @param several 保留小数位
     */
    public static String fourUpFiveInto(String str,int several){
        BigDecimal b1 = new BigDecimal(str);
        BigDecimal b2 = b1.setScale(several, BigDecimal.ROUND_HALF_UP);
        return b2.toString();
    }

    /**
     * 舍去保留小数位后面的数字
     * @param str
     * @param several 保留小数位
     */
    public static String into(String str,int several){
        BigDecimal b1 = new BigDecimal(str);
        BigDecimal b2 = b1.setScale(several, BigDecimal.ROUND_HALF_DOWN);
        return b2.toString();
    }

    /**
     * 四舍六入五成双
     * @param str
     * @param several 保留小数位
     */
    public static String fourUpSixInto(String str,int several){
        BigDecimal b1 = new BigDecimal(str);
        BigDecimal b2 = b1.setScale(several, BigDecimal.ROUND_HALF_EVEN);
        return b2.toString();
    }

}
