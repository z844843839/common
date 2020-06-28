package com.crt.common.util;

import com.crt.common.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import static java.math.BigDecimal.*;

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
     * @param addend1
     * @param addend1
     * @return
     */
    public static BigDecimal add(BigDecimal addend1,BigDecimal addend2) {
        BigDecimal result = new BigDecimal(0);
        if (null != addend1 && null != addend2){
            result = addend1.add(addend2);
        }
        return result;
    }

    /**
     * 两个数相乘
     * @param multiplier1
     * @param multiplier2
     * @return
     */
    public static BigDecimal multiply(BigDecimal multiplier1,BigDecimal multiplier2) {
        BigDecimal result = new BigDecimal(0);
        if (null != multiplier1 && null != multiplier2){
            result = multiplier1.multiply(multiplier2);
            return result;
        }
        return result;
    }

    /**
     * 两个数相减
     * @param sub1 减数1
     * @param sub2 减数2
     * @return
     */
    public static BigDecimal subtract(BigDecimal sub1,BigDecimal sub2) {
        BigDecimal result = new BigDecimal(0);
        if (null != sub1 && null != sub2){
            result = sub1.subtract(sub2);
        }
        return result;
    }

    /**
     * 两个数相除
     * @param divisor 除数
     * @param dividend 被除数
     * @param keepType 结果保留方式（1 四舍五入 2 四舍六入五成双 3 直接舍去保留意外的数字）
     * @param several 保留小数位
     * @return
     */
    public static BigDecimal divide(BigDecimal divisor,BigDecimal dividend,int keepType,int several) {
        BigDecimal result = new BigDecimal(0);
        if (null != divisor && null != dividend){
            if (dividend.compareTo(new BigDecimal(0)) == 0){
                return result;
            }else {
                switch (keepType){
                    case 1:
                        result = divisor.divide(dividend,several,ROUND_HALF_UP);
                        break;
                    case 2:
                        result = divisor.divide(dividend,several,ROUND_HALF_EVEN);
                        break;
                    case 3:
                        result = divisor.divide(dividend,several,ROUND_HALF_DOWN);
                        break;
                }
                return result;
            }
        }
        return result;
    }

}
