package com.crt.common.i18n;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.text.MessageFormat;
import java.util.*;

/**
 * 自定义国际化的核心类，
 * 参考两个资料：
 * https://blog.csdn.net/banqingyang/article/details/49491693
 * https://blog.csdn.net/blueheart20/article/details/78115915
 * @Author liupengfei@e6yun.com
 * @Date 2019/1/30 14:46
 * @Description
 **/
public class E6MessageResource extends AbstractMessageSource implements ResourceLoaderAware {

    static Logger logger = LoggerFactory.getLogger(E6MessageResource.class);

    @SuppressWarnings("unused")
    private ResourceLoader resourceLoader;

    /**
     * 所有应用提供的国际化加载器
     */
    List<E6I18nDataLoadService> e6I18nDataLoadServices;
    /**
     * 国际化字段内容
     */
    private static Map<Locale, Map<String,String>> localeDictsMap = new HashMap<>();

    /**
     * 默认语言类型
     */
    static Locale defaultLocale = Locale.CHINESE;

    public E6MessageResource() {
    }

    public void reload() {
        Map<Locale, Map<String,String>> newLocaleDictsMap = loadTexts();
        //得到新的字典后再修改静态变量的指针，这个过程就可以在系统运行中多次执行,支持后台编辑国际化数据库字典，修改后触发重新加载即可
        localeDictsMap = newLocaleDictsMap;
    }

    /**
     * 加载数据
     * @return
     */
    protected Map<Locale,Map<String,String>> loadTexts() {
        Map<Locale, Map<String,String>> localeMapResource = new HashMap<>();
        if(e6I18nDataLoadServices==null || e6I18nDataLoadServices.isEmpty()){
            return localeMapResource;
        }
        //遍历内存中所有的E6I18nDataLoadService的实现类，逐一进行执行
        for(E6I18nDataLoadService e6I18nDataLoadService:e6I18nDataLoadServices){
            logger.info("从{}加载国际化资源",e6I18nDataLoadService.getClass().getName());
            List<E6I18nDictByLocaleEntity> e6I18nDictByLocaleEntities = e6I18nDataLoadService.loadE6I18nDictByLocaleEntity();
            if(e6I18nDictByLocaleEntities==null || e6I18nDictByLocaleEntities.isEmpty()){
                continue;
            }
            for(E6I18nDictByLocaleEntity e6I18nDictByLocaleEntity:e6I18nDictByLocaleEntities){
                Locale locale = e6I18nDictByLocaleEntity.getLocale();
                Map<String,String> entityKVMap = localeMapResource.get(locale);
                if(entityKVMap==null){
                    entityKVMap = e6I18nDictByLocaleEntity.getE6I18nDictKVMap();
                    localeMapResource.put(locale,entityKVMap);
                }else{
                    entityKVMap.putAll(e6I18nDictByLocaleEntity.getE6I18nDictKVMap());
                }
            }
        }
        return localeMapResource;
    }


    /**
     * 从指定locale获取值
     * @param code
     * @param locale
     * @return
     */
    private String findValueFromLocale(String code,Locale locale){
        Map<String,String> entityKVMap =localeDictsMap.get(locale);
        if(entityKVMap==null) {
            return null;
        }
        String resultValue = entityKVMap.get(code);
        if(logger.isDebugEnabled()){
            logger.debug("DB从{}找到变量{}={}", locale.toString(), code, resultValue);
        }
        return resultValue;
    }

    /**
     * 这是加载国际化变量的核心方法，先从自己控制的内存中取，取不到了再到资源文件中取
     * @param code
     * @param locale 本地化语言
     * @return
     */
    private String getText(String code, Locale locale) {
        String resultValue = null;
        //第一种情况，通过期望的语言类型查找
        resultValue =findValueFromLocale(code,locale);
        if(StringUtils.isNotEmpty(resultValue)){
            return resultValue;
        }
        //第二种情况，如果期望是 语言-国家 没有找到，那么尝试只找一下语言,比如zh-tw没找到，那就尝试找一下zh
        if(locale.getCountry()!=null){
            Locale localeOnlyLanguage = Locale.forLanguageTag(locale.getLanguage());
            resultValue =findValueFromLocale(code,localeOnlyLanguage);
            if(StringUtils.isNotEmpty(resultValue)){
                return resultValue;
            }
        }
        //第三种情况，如果没有找到 且不是默认语言包，则取默认语言包
        if(!Objects.equals(locale,defaultLocale)){
            resultValue =findValueFromLocale(code,defaultLocale);
            if(StringUtils.isNotEmpty(resultValue)){
                return resultValue;
            }
        }
        //第四种情况，通过以上三种方式都没找到，那么尝试从本地配置文件加载期望的语言类型是否有
        if (getParentMessageSource() != null) {
            resultValue = getParentMessageSource().getMessage(code, null,null, locale);
            if(StringUtils.isNotEmpty(resultValue)){
                logger.debug("从配置文件{}找到变量{}={}", locale.toString(), code, resultValue);
                return resultValue;
            }
        }
        //第五种情况，如果没有找到，则从本地配置文件加载默认的语言类型是否有
        if(!Objects.equals(locale,defaultLocale)){
            resultValue = getParentMessageSource().getMessage(code, null,null, defaultLocale);
            logger.debug("从配置文件找默认语言包{}查找变量{}={}", defaultLocale.toString(), code, resultValue);
        }
        //如果最终还是取不到，返回了NULL，则外面会用默认值，如果没有默认值，最终会返回给页面变量名称,所以变量名称尽量有含义，以作为遗漏配置的最后保障
        return resultValue;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = (resourceLoader != null ? resourceLoader : new DefaultResourceLoader());
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        String msg = getText(code, locale);
        MessageFormat result = createMessageFormat(msg, locale);
        return result;
    }

    @Override
    protected String resolveCodeWithoutArguments(String code, Locale locale) {
        String result = getText(code, locale);
        return result;
    }

    public void setE6I18nDataLoadServices(List<E6I18nDataLoadService> e6I18nDataLoadServices) {
        this.e6I18nDataLoadServices = e6I18nDataLoadServices;
    }
}
