package com.crt.common.i18n;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author liupengfei@e6yun.com
 * @Date 2019/1/30 11:48
 * @Description
 **/
@Component
@ConditionalOnProperty(prefix = "crt.common.i18n",name = "enable",matchIfMissing = true)
public class I18nDictUtil implements WebMvcConfigurer {
    static Logger logger = LoggerFactory.getLogger(I18nDictUtil.class);

    @Autowired
    private static E6MessageResource messageSource;
    /**
     * 缓存有效时间
     */
    static long cacheTimeMs = 10*60*1000;

    static ThreadLocal<Locale> LocaleThreadLocal = new ThreadLocal<>();

    @Autowired
    public void setMessageSource(E6MessageResource messageSource) {
        I18nDictUtil.messageSource = messageSource;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加用于国际化语言转换的拦截器
        registry.addInterceptor(new HandlerInterceptor(){
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                Locale locale = RequestContextUtils.getLocale(request);
                LocaleThreadLocal.set(locale);
                return true;
            }
        });
    }

    /**
     * @param keyAndDefaultVal 国际化变量名称，同时也是默认值
     * @return
     */
    public static String getI18nValue(String keyAndDefaultVal) {
        if (StringUtils.isEmpty(keyAndDefaultVal)) {
            return StringUtils.EMPTY;
        }
        Locale locale = LocaleThreadLocal.get();
        if (locale == null) {
            locale = Locale.CHINA;
        }
        return MessageCache.getValue(locale,keyAndDefaultVal);
    }

    /**
     * 国际化内容缓存
     */
    private static class MessageCache{
        static Map<Locale,Map<String,MessageCacheEntity>> i18nCacheMap = new ConcurrentHashMap<>();

        /**
         * 从缓存获取国际化值
         * @param locale
         * @param keyAndDefaultVal
         * @return
         */
        public static String getValue(Locale locale, String keyAndDefaultVal) {
            Map<String, MessageCacheEntity> localeMap = i18nCacheMap.get(locale);
            if (localeMap == null) {
                localeMap = new ConcurrentHashMap<>();
                i18nCacheMap.put(locale, localeMap);
            }
            MessageCacheEntity messageCacheEntity = localeMap.get(keyAndDefaultVal);
            String value = null;
            //1 - 没有找到，初始化
            if (messageCacheEntity == null) {
                value = messageSource.getMessage(keyAndDefaultVal, null, keyAndDefaultVal, locale);
                messageCacheEntity = new MessageCacheEntity(value, System.currentTimeMillis());
                localeMap.put(keyAndDefaultVal, messageCacheEntity);
                logger.debug("i18nCache init:{}[{}]={}",keyAndDefaultVal,locale.getDisplayName(),value);
            } else {
                value = messageCacheEntity.getValue();
                //2 - 找到了，判断缓存是否过期
                if (System.currentTimeMillis() - messageCacheEntity.getInsertTime() > cacheTimeMs) {
                    value = messageSource.getMessage(keyAndDefaultVal, null, keyAndDefaultVal, locale);
                    messageCacheEntity.setValue(value);
                    messageCacheEntity.setInsertTime(System.currentTimeMillis());
                    logger.debug("i18nCache refresh:{}[{}]={}",keyAndDefaultVal,locale.getDisplayName(),value);
                }
            }
            return StringUtils.isEmpty(value) ? keyAndDefaultVal : value;
        }
    }

    @Value("${e6.i18n.cacheTimeMs:600000}")
    public void setCacheTimeMs(long cacheTimeMs) {
        I18nDictUtil.cacheTimeMs = cacheTimeMs;
    }

    /**
     * I18n值的实体类
     */
    private static class MessageCacheEntity{
        /**
         * I18n的值
         */
        String value;
        /**
         * 更新时间
         */
        long insertTime;

        public MessageCacheEntity(String value, long insertTime) {
            this.value = value;
            this.insertTime = insertTime;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public long getInsertTime() {
            return insertTime;
        }

        public void setInsertTime(long insertTime) {
            this.insertTime = insertTime;
        }
    }
}
