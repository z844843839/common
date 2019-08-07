package com.crt.commones.es.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 模板引擎实用类
 *
 * @author dreamingo
 */
public class TemplateUtil {

    /**
     * 模板变量Pattern
     */
    private static Pattern pattern = Pattern.compile("\\@\\{[0-9a-zA-Z_.]+\\}");

    /**
     * 获取变量列表
     *
     * @param template 模板字符串
     * @return 变量列表
     */
    public static List<String> getVarList(String template) {
        List<String> varList = new ArrayList<>();
        Matcher m = pattern.matcher(template);
        while (m.find()) {
            String param = m.group();
            varList.add(param.substring(2, param.length() - 1));
        }
        return varList;
    }

    /**
     * 执行模板替换
     *
     * @param template 模板字符串
     * @param params   参数
     * @return 替换后的字符串
     */
    public static String processTemplate(String template, Map<String, Object> params) {
        StringBuffer sb = new StringBuffer();
        Matcher m = pattern.matcher(template);
        while (m.find()) {
            String param = m.group();
            Object value = params.get(param.substring(2, param.length() - 1));
            m.appendReplacement(sb, value == null ? "" : SqlUtil.filter(value.toString()));
        }
        m.appendTail(sb);
        return sb.toString();
    }

}
