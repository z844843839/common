package com.crt.common.loadbalance;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import org.springframework.context.annotation.Bean;

/**
 * @Author liupengfei@e6yun.com
 * @Date 2019/6/6 8:49
 * @Description 这个类不加注解，所以不会自动被加载，需要一个Configuration类来用注解加载：@RibbonClients(defaultConfiguration = E6DefaultRibbonConfig.class)
 **/
public class E6DefaultRibbonConfig {
    @Bean
    public ILoadBalancer ribbonLoadBalancer(IClientConfig config, ServerList<Server> serverList, ServerListFilter<Server> serverListFilter, IRule rule, IPing ping, ServerListUpdater serverListUpdater,ConfigFileLoader configFileLoader) {
        return new E6LoadBalancer(config, rule, ping, serverList, serverListFilter, serverListUpdater,configFileLoader);
    }
    @Bean
    public IRule ribbonRule(ConfigFileLoader configFileLoader) {
        E6LoadBalancerRule myRule = new E6LoadBalancerRule();
        myRule.init(configFileLoader);
        return myRule;
    }

}