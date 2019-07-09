package com.crt.common.apollo.config;

import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @Description  默认加载的配置类
 * @Author changyandong@e6yun.com
 * @Created Date: 2018/8/8 9:28
 * @ClassName ApolloConfig
 * @Version: 1.0
 */
@ConditionalOnProperty(prefix = "apollo.bootstrap",name = "enabled",havingValue = "true",matchIfMissing = true)
@Configuration
@EnableApolloConfig
public class ApolloConfig {

    Logger logger = LoggerFactory.getLogger(ConfigChangeService.class);

    @Autowired(required = false)
    ConfigChangeService configChangeService;

    /**
     * 检测配置变化
     *
     * @param changeEvent
     */
    @ApolloConfigChangeListener
    public void configChangeListener(ConfigChangeEvent changeEvent) {
        //配置变化处理
        if(configChangeService == null){
            configChangeService = changeEvent1 -> {
                for (String key : changeEvent1.changedKeys()) {
                    ConfigChange change = changeEvent1.getChange(key);
                    logger.info("发现变化 - key: {}, oldValue: {}, newValue: {}, changeType: {}", change.getPropertyName(), change.getOldValue(), change.getNewValue(), change.getChangeType());
                }
            };
        }
        configChangeService.detectConfigChanges(changeEvent);
    }
}
