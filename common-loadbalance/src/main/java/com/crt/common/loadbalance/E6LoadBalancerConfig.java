package com.crt.common.loadbalance;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author liupengfei@e6yun.com
 * @Created Date: 2019/6/6 9:58
 * @Description 自定义loadBalance开关和配置类，默认关闭，需要通过设置e6.rpc.config.local.enable=true打开这个功能
 */
@ConditionalOnProperty(value = "crt.rpc.config.local.enable",havingValue = "true",matchIfMissing = false)
@Configuration
@RibbonClients(defaultConfiguration = E6DefaultRibbonConfig.class)
public class E6LoadBalancerConfig {

    @Bean
    public ConfigFileLoader initConfigFileLoader(){
        ConfigFileLoader configFileLoader = new ConfigFileLoader();
        configFileLoader.initialize();
        return configFileLoader;
    }
}
