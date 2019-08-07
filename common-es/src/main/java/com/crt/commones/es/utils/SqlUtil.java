package com.crt.commones.es.utils;

/**
 * SQL 工具类
 *
 * @author dremaingo
 */
public class SqlUtil {

    /** %|--|use|and|or|not| */
    public final static String regex = "'|;|insert|delete|update|select|count|group|union" +
            "|create|drop|truncate|alter|grant|execute|exec|xp_cmdshell|call|declare|source|sql";

    /**
     * 把SQL关键字替换为空字符串
     *
     * @param param
     * @return
     */
    public static String filter(String param) {
        if (param == null) {
            return param;
        }
        //(?i)不区分大小写替换
        return param.replaceAll("(?i)" + regex, "");
    }

    public static String transact(String param) {
        return param.replaceAll(".*([';]+|(--)+).*", " ");
        //replaceAll("([';])+|(--)+","");
    }

}
