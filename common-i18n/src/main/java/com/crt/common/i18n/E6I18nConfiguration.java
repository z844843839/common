package com.crt.common.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.List;
import java.util.Locale;

/**
 * @Author liupengfei@e6yun.com
 * @Date 2019/1/30 14:53
 * @Description
 **/
@Configuration
@ConditionalOnProperty(prefix = "crt.common.i18n",name = "enable",matchIfMissing = true)
public class E6I18nConfiguration {
    static Logger logger = LoggerFactory.getLogger(E6I18nConfiguration.class);

    /**
     * 采用默认的配置文件配置 messages开头的文件，编码为utf8
     * 如 messages_zh_CN.properties ,  messages_en_US.properties
     * @returns
     */
    @Bean
    public MessageSourceProperties messageSourceProperties() {
        return new MessageSourceProperties();
    }

    @Bean
    public ResourceBundleMessageSource initResourceBundleMessageSource(MessageSourceProperties messageSourceProperties){
        ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setBasename(messageSourceProperties.getBasename());
        resourceBundleMessageSource.setDefaultEncoding(messageSourceProperties.getEncoding().name());
        return resourceBundleMessageSource;
    }

    @Bean
    public SessionLocaleResolver initSessionLocaleResolver(){
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        //默认用中文简体
        sessionLocaleResolver.setDefaultLocale(Locale.CHINA);
        return sessionLocaleResolver;
    }

    @Bean
    public E6MessageResource initE6MessageResource(@Autowired(required = false) ResourceBundleMessageSource resourceBundleMessageSource,
                                                   @Autowired(required = false) List<E6I18nDataLoadService> e6I18nDataLoadServices){
        E6MessageResource e6MessageResource = new E6MessageResource();
        e6MessageResource.setParentMessageSource(resourceBundleMessageSource);
        e6MessageResource.setE6I18nDataLoadServices(e6I18nDataLoadServices);
        new Thread(()->e6MessageResource.reload()).start();
        return e6MessageResource;
    }

    @Autowired
    @Lazy
    E6MessageResource e6MessageResource;

    /**
     * 默认每个整点运行一次，从数据库重新加载
     */
    @Scheduled(cron = "${e6.i18n.reload.cron:0 0 0/1 * * ?}")
    public void reloadI18n(){
        try {
            logger.debug("in reloadI18n");
            e6MessageResource.reload();
        }catch (Exception ex){
            logger.error("reloadI18n异常",ex);
        }
    }
}
