package com.crt.common.i18n;

import java.util.Locale;
import java.util.Map;

/**
 * 每个语言对应的常量字典
 * @Author liupengfei@e6yun.com
 * @Date 2019/1/30 16:37
 * @Description
 **/
public class E6I18nDictByLocaleEntity {
    /**
     * 语言类型
     */
    private Locale locale;
    /**
     * 所有变量的k-v键值对
     */
    private Map<String,String> e6I18nDictKVMap;

    public E6I18nDictByLocaleEntity() {
    }

    public E6I18nDictByLocaleEntity(Locale locale, Map<String, String> e6I18nDictKVMap) {
        this.locale = locale;
        this.e6I18nDictKVMap = e6I18nDictKVMap;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Map<String, String> getE6I18nDictKVMap() {
        return e6I18nDictKVMap;
    }

    public void setE6I18nDictKVMap(Map<String, String> e6I18nDictKVMap) {
        this.e6I18nDictKVMap = e6I18nDictKVMap;
    }
}
